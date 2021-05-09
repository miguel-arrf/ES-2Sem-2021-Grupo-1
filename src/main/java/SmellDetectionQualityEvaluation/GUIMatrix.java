package SmellDetectionQualityEvaluation;


import g1.ISCTE.AppStyle;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;

public class GUIMatrix {
	private final Label truePositivesLabel = new Label("0");
	private final Label falsePositivesLabel = new Label("0");
	private final Label trueNegativesLabel = new Label("0");
	private final Label falseNegativesLabel = new Label("0");

	/**
	* Every label in the confusion matrix is setted to 0.
	*/
	public void resetLabelValues() {
		truePositivesLabel.setText("0");
		falsePositivesLabel.setText("0");
		falseNegativesLabel.setText("0");
		trueNegativesLabel.setText("0");
	}

	/**
	* Method in charge of creating the graphical representation of the confusion matrix using a gridPane.
	* @return  a Pane that represents a confusion matrix.
	*/
	public Pane createMatrix() {
		Pane truePositivesPane = createMatrixPanel("True Positive", truePositivesLabel);
		Pane falsePositivesPane = createMatrixPanel("False Positive", falsePositivesLabel);
		Pane falseNegativesPane = createMatrixPanel("False Negative", falseNegativesLabel);
		Pane trueNegativesPane = createMatrixPanel("True Negative", trueNegativesLabel);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));

		setColumnConstraints(grid);

		addToGrid(truePositivesPane, grid, 0, 0);
		addToGrid(falsePositivesPane, grid, 0, 1);
		addToGrid(falseNegativesPane, grid, 1, 0);
		addToGrid(trueNegativesPane, grid, 1, 1);

		VBox matrix = new VBox(grid);

		matrix.setBorder(new Border(new BorderStroke(Color.web(AppStyle.grayTextColor), BorderStrokeStyle.DASHED, new CornerRadii(7),
				new BorderWidths(2))));
		matrix.setBackground(new Background(
				new BackgroundFill(Color.web("rgba(118,116,126,0.3)"), new CornerRadii(7), Insets.EMPTY)));
		return matrix;
	}

	/**
	* Updates the confusion matrix graphical representation with the correct values from the analysis.
	* @param matrix  the confusion matrix
	*/
	public void updateLabelValues(ConfusionMatrix matrix) {
		truePositivesLabel.setText(Integer.toString(matrix.getTruePositives()));
		falsePositivesLabel.setText(Integer.toString(matrix.getFalsePositives()));
		falseNegativesLabel.setText(Integer.toString(matrix.getFalseNegatives()));
		trueNegativesLabel.setText(Integer.toString(matrix.getTrueNegatives()));
	}

	/**
	* Sets the first two columns of a given gridpane as occupying 50% each of the grid size.
	* @param gridPane  the gridpane to which we want to apply the column constraints
	*/
	public void setColumnConstraints(GridPane gridPane) {
		ColumnConstraints columnConstraints1 = new ColumnConstraints();
		columnConstraints1.setPercentWidth(50);
		ColumnConstraints columnConstraints2 = new ColumnConstraints();
		columnConstraints2.setPercentWidth(50);
		gridPane.getColumnConstraints().addAll(columnConstraints1, columnConstraints2);
	}

	/**
	* Adds a given node in a given gridPane in a given position defined by a row and a column.
	* @param node  the node to be added to the gridPane.
	* @param gridPane  the gridPane to where the items shall be added.
	* @param row  the row to put the node.
	* @param column  the column to put the node.
	*/
	public void addToGrid(Pane node, GridPane gridPane, int row, int column) {
		GridPane.setRowIndex(node, row);
		GridPane.setColumnIndex(node, column);
		gridPane.getChildren().add(node);
	}

	/**
	* Creates a confusion matrix label with the correct design. Associating to each row/column in the confusion matrix a description (cellName) and the value (result of the analysis - label).
	* @param cellName  the cell description.
	* @param label  the label containing the value of the analysis.
	* @return  a Pane representing the description and value of each of the rows and columns of the confusion matrix.
	*/
	public Pane createMatrixPanel(String cellName, Label label) {
		Label cellNameLabel = new Label(cellName);
		cellNameLabel.setTextFill(Color.WHITE);
		label.setTextFill(Color.WHITE);
		VBox box = new VBox(cellNameLabel, label);
		box.setSpacing(20);
		box.setPadding(new Insets(10));
		return box;
	}
}