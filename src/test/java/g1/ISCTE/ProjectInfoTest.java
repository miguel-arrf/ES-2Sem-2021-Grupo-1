package g1.ISCTE;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProjectInfoTest {

    @Test
    void createWorkbook() throws IOException, URISyntaxException {
        String path = "C:\\Users\\Chainz\\ProjetosGit\\ES-2Sem-2021-Grupo-1\\src\\main\\resources\\ProjectInfoTestFolder\\teste.xlsx";
        File file = new File(path);
        FileInputStream fip = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fip);
        assertNotNull(workbook);
    }

    @Test
    void packageCounter() throws IOException {
        ProjectInfo projectInfo = new ProjectInfo(ProjectInfo.createWorkbook("C:\\Users\\Chainz\\ProjetosGit\\ES-2Sem-2021-Grupo-1\\src\\main\\resources\\ProjectInfoTestFolder\\teste.xlsx"));
        int count = projectInfo.packageCounter();
        assertNotNull(count);
        assertEquals(10, count);
    }

    @Test
    void classCounter() throws IOException {
        ProjectInfo projectInfo = new ProjectInfo(ProjectInfo.createWorkbook("C:\\Users\\Chainz\\ProjetosGit\\ES-2Sem-2021-Grupo-1\\src\\main\\resources\\ProjectInfoTestFolder\\teste.xlsx"));
        int count = projectInfo.classCounter();
        assertNotNull(count);
        assertEquals(61, count);
    }

    @Test
    void methodCounter() throws IOException {
        ProjectInfo projectInfo = new ProjectInfo(ProjectInfo.createWorkbook("C:\\Users\\Chainz\\ProjetosGit\\ES-2Sem-2021-Grupo-1\\src\\main\\resources\\ProjectInfoTestFolder\\teste.xlsx"));
        int count = projectInfo.methodCounter();
        assertNotNull(count);
        assertEquals(360, count);
    }

    @Test
    void lineCounter() throws IOException {
        ProjectInfo projectInfo = new ProjectInfo(ProjectInfo.createWorkbook("C:\\Users\\Chainz\\ProjetosGit\\ES-2Sem-2021-Grupo-1\\src\\main\\resources\\ProjectInfoTestFolder\\teste.xlsx"));
        int count = projectInfo.lineCounter();
        assertNotNull(count);
        assertEquals(7223, count);
    }

    @Test
    void getMetricsTable() throws IOException {
        ProjectInfo projectInfo = new ProjectInfo(ProjectInfo.createWorkbook("C:\\Users\\Chainz\\ProjetosGit\\ES-2Sem-2021-Grupo-1\\src\\main\\resources\\ProjectInfoTestFolder\\teste.xlsx"));
        ArrayList<ArrayList<String>> table = projectInfo.getMetricsTable();

        ArrayList<ArrayList<String>> tableExpected = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        XSSFSheet sheet = ProjectInfo.createWorkbook("C:\\Users\\Chainz\\ProjetosGit\\ES-2Sem-2021-Grupo-1\\src\\main\\resources\\ProjectInfoTestFolder\\teste.xlsx").getSheetAt(0);

        for(Row row : sheet) {
            ArrayList<String> line = new ArrayList<>(11);
            for(int column=0; column < row.getLastCellNum(); column++) {
                Cell cell = row.getCell(column);
                String cellValue = dataFormatter.formatCellValue(cell);
                line.add(cellValue);
            }
            tableExpected.add(line);
        }

        assertNotNull(table);
        for(int line = 0; line != table.size(); line++) {
            assertLinesMatch(tableExpected.get(line), table.get(line));
        }
    }

    @Test
    void getMainMetricsInfo() throws IOException {
        ProjectInfo projectInfo = new ProjectInfo(ProjectInfo.createWorkbook("C:\\Users\\Chainz\\ProjetosGit\\ES-2Sem-2021-Grupo-1\\src\\main\\resources\\ProjectInfoTestFolder\\teste.xlsx"));

        String[] metricsExpected = new String[4];
        metricsExpected[0] = Integer.toString(projectInfo.packageCounter());
        metricsExpected[1] = Integer.toString(projectInfo.classCounter());
        metricsExpected[2] = Integer.toString(projectInfo.methodCounter());
        metricsExpected[3] = Integer.toString(projectInfo.lineCounter());

        String[] metrics = projectInfo.getMainMetricsInfo();

        assertNotNull(metrics);
        assertArrayEquals(metricsExpected, metrics);
    }
}