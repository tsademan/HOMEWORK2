import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

;

public class MyController {


    String data1, data2;
    DbConnection obj = new DbConnection();
    Connection conn = obj.connMethod();
    @FXML
    private TextField txt1;
    @FXML
    private TextField txt2;
    @FXML
    private GridPane gp;
    private ObservableList<ObservableList> data;
    @FXML
    private TableView tbl;
    TableColumn col;
    private String s;

    public void buildData() {
        DbConnection obj1;
        Connection c;
        ResultSet rs;
        data = FXCollections.observableArrayList();
        try {

            tbl.setStyle("-fx-background-color:red; -fx-font-color:yellow ");
            obj1 = new DbConnection();
            c = obj1.connMethod();
            String SQL = "SELECT * from STUDENT2";
            rs = c.createStatement().executeQuery(SQL);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                 col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                        ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                tbl.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");

            }
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                System.out.println("Row[1]added " + row);
                data.add(row);

            }

            tbl.setItems(data);
            PdfGenerate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error ");
        }
    }
    public void handleButtonAction(ActionEvent event) {

        data1 = txt1.getText();
        data2 = txt2.getText();

        String query = "Insert into STUDENT2(NAME,COURSE) VALUES('" + data1 + "','" + data2 + "')";
        try {

            Statement statement = conn.createStatement();
            boolean status=statement.execute(query);
            txt1.setText("");
            txt2.setText("");

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            if (!status)
            a.setContentText("successfuly inserted");
            else
                a.setContentText("Failed");
            a.showAndWait();
            buildData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void PdfGenerate(){
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("D:/data.pdf"));
            document.open();
            document.add(new Paragraph(String.valueOf(col)));
            document.add(new Paragraph(String.valueOf(data)));

            document.close();
        } catch (FileNotFoundException | DocumentException e) {
            System.out.println(e);
        }
        System.out.println("the test for pdf is successful");
    }
}
