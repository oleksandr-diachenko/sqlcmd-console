package ua.com.juja.positiv.sqlcmd.databasemanager;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class DatabaseManagerTest {

    DatabaseManager manager = new JDBCDatabaseManager();

    @Before
    public void run() throws SQLException, ClassNotFoundException {
        manager.connect("sqlcmd", "postgres", "123");

        try {
            manager.drop("car");
            manager.drop("client");
        }catch (SQLException e){
            //если таблиц еще нету do nothing
        }

        Map<String, Object> tableCar = new LinkedHashMap<>();
        tableCar.put("name", "text");
        tableCar.put("color", "text");
        tableCar.put("age", "int");
        manager.table("car", "id", tableCar);

        Map<String, Object> field1 = new HashMap<>();
        field1.put("id", 1);
        field1.put("name", "ferrari");
        field1.put("color", "red");
        field1.put("age", 6);
        manager.create("car", field1);

        Map<String, Object> field2 = new HashMap<>();
        field2.put("id", 2);
        field2.put("name", "porsche");
        field2.put("color", "black");
        field2.put("age", 1);
        manager.create("car", field2);

        Map<String, Object> field3 = new HashMap<>();
        field3.put("id", 3);
        field3.put("name", "bmw");
        field3.put("color", "blue");
        field3.put("age", 3);
        manager.create("car", field3);

        Map<String, Object> tableClient = new LinkedHashMap<>();
        manager.table("client", "id", tableClient);
    }

    @Test
    public void testDelete_WithCorrectData() throws SQLException {
        manager.delete("car", "id", "3");

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                "1, ferrari, red, 6, " +
                "2, porsche, black, 1]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testDelete_WithIncorrectData_TableName() throws SQLException {
        manager.delete("qwe", "id", "3");
    }

    @Test
    public void testGetTableNames() throws Exception {
        Set<String> tableNames = manager.getTableNames();
        assertEquals("[car, client]", tableNames.toString());
    }

    @Test
    public void testFind_WithCorrectData() throws SQLException {
        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                "1, ferrari, red, 6, " +
                "2, porsche, black, 1, " +
                "3, bmw, blue, 3]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testFind_WithIncorrectData_TableName() throws SQLException {
        manager.getTableData("qwe");
    }

    @Test
    public void testFindLimitOffset_WithCorrectData() throws SQLException {
        List<String> tableData = manager.getTableData("car LIMIT 2 OFFSET 1");
        assertEquals("[4, id, name, color, age, " +
                "2, porsche, black, " +
                "1, 3, bmw, blue, 3]", tableData.toString());
    }

    @Test
    public void testUpdateAll_WithCorrectData() throws SQLException {
        Map<String, Object> columnData = new LinkedHashMap<>();
        columnData.put("name", "mercedes");
        columnData.put("color", "white");
        columnData.put("age", "10");
        manager.update("car", "id", "3", columnData);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                "1, ferrari, red, 6, " +
                "2, porsche, black, 1, " +
                "3, mercedes, white, 10]", tableData.toString());
    }

    @Test
    public void testUpdateSingle_WithCorrectData() throws SQLException {
        Map<String, Object> columnData = new LinkedHashMap<>();
        columnData.put("name", "mercedes");
        manager.update("car", "id", "3", columnData);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                "1, ferrari, red, 6, " +
                "2, porsche, black, 1, " +
                "3, mercedes, blue, 3]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testUpdate_WithIncorrectData_TableName() throws SQLException {
        Map<String, Object> columnData = new LinkedHashMap<>();
        columnData.put("name", "mercedes");
        manager.update("qwe", "id", "3", columnData);
    }

    @Test
    public void testClear_WithCorrectData() throws SQLException {
        manager.clear("car");

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age]", tableData.toString());
    }

    @Test(expected = SQLException.class)
    public void testClear_WithIncorrectData_TableName() throws SQLException {
        manager.clear("qwe");
    }

    @Test
    public void testCreateAll_WithCorrectData() throws SQLException {
        manager.clear("car");
        Map<String, Object> data = new HashMap<>();
        data.put("id", "1");
        data.put("name", "ferrari");
        data.put("color", "red");
        data.put("age", "6");
        manager.create("car", data);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                "1, ferrari, red, 6]", tableData.toString());
    }

    @Test
    public void testCreateSingle_WithCorrectData() throws SQLException {
        manager.clear("car");
        Map<String, Object> data = new HashMap<>();
        data.put("id", "2");
        manager.create("car", data);

        List<String> tableData = manager.getTableData("car");
        assertEquals("[4, id, name, color, age, " +
                "2, , , ]", tableData.toString());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testCreate_WithIncorrectData_Length() throws SQLException {
        Map<String, Object> map = new HashMap<>();
        manager.create("qwe", map);
    }

    @Test(expected = SQLException.class)
    public void testCreate_WithIncorrectData_TableName() throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("id", "2");
        manager.create("qwe", data);
    }

    @Test
    public void testTable_WithCorrectData() throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "text");
        data.put("population", "int");
        data.put("county", "text");
        manager.table("city", "id", data);

        Set<String> tableNames = manager.getTableNames();
        assertEquals("[car, city, client]", tableNames.toString());
        manager.drop("city");
    }

    @Test(expected = SQLException.class)
    public void testTable_WithIncorrectData_Type() throws SQLException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "");
        manager.table("city", "id", data);
    }
}
