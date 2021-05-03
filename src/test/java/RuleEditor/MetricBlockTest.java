package RuleEditor;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricBlockTest {

    private final JFXPanel panel = new JFXPanel();

    private static MetricBlock metricBlock;
    private static final String metricString = "testMetric";

    @BeforeAll
    private static void setUP(){
        JFXPanel javaFXHelper = new JFXPanel();
        metricBlock = new MetricBlock(metricString);

    }

    @Test
    void getGraphicalRepresentation() {
        assertNotNull(metricBlock.getGraphicalRepresentation());
    }

    @Test
    void getHBox() {
        assertNotNull(metricBlock.getHBox());
    }

    @Test
    void getMetricMessage() {
        assertEquals(metricString, metricBlock.getMetricMessage());
    }

    @Test
    void getType() {
        assertEquals(metricBlock.getType(), Types.MetricBlock);
    }

    @Test
    void getWidgetGraphicalRepresentation() {
        assertNotNull(metricBlock.getWidgetGraphicalRepresentation());
    }

    @Test
    void getOperator() {
        assertNull(metricBlock.getOperator());
    }

    @Test
    void getCopy() {
        CustomNode copy = metricBlock.getCopy();
        assertEquals(copy, metricBlock);
    }
}