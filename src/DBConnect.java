package com.prediction;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
/**JFrame display.
 * This is a UI class that calls logic from other classes.
 * @author Nirvi Badyal
 * @version 13
 **/

public class DBConnect extends JFrame {
	DisplayAddSample displayAddSample;
	TrainModel TrainModel;
	TestModel TestModel;
	Evaluate eval;
	DecisionTree predict;
	JPanel panelPredict;
	
	JPanel panelHoldJButtons;		//holds buttons
	JTextArea textAreaResult;		//holds area entry of query by user
	JButton buttonTrain;				//submits query to view train data set
	JButton buttonTest; 				//submits query to view test data set
	JButton buttonExit;				//exit 
	TableQuery tQuery;
	
	JPanel panelResultDisplay; 		//holds options for model selection (C45Tree NN tree)
	JRadioButton choiceC45Tree;			//to select C45Tree
	JRadioButton choiceC45Algo;			//to select C4.5 
	JRadioButton choiceTree;			//to select decision tree
	ButtonGroup buttonGroup; 		//holds buttons for svm nn tree
	
	JPanel panelTrainTestData;		//holds datasets of training and testing samples
	
	JButton buttonEvaluate;
	JButton buttonpredict;
	
	MakeFile makeFile ;				//to make files for training and testing 
	CompareTwoArrays compareTwoArrays; //to call methods to compare results to the answer key
	
	JPanel panelTestTrainResButtons;	//panel to hold data sets for training, testing, results, and radio buttons
	JPanel panelTrainTest;			//holds train and test data sets
	JPanel panelRadioButtons;   		// holds radio buttons
	
	PrepareTableTree prepareTableTree;	//instantiation of classes
	PrepareTableC45Tree prepareTableC45Tree;
	PrepareTableNN prepareTableNN;
	
	JPanel panelNorth;
	JPanel panelTop;
	
	JPanel panelEquator;
	JPanel panelMiddle;

	JTextArea textAreaPredict;
	JButton buttonPredict;
	
	//constructor
	public DBConnect()
	{	
		predict = new Predict();
		
		
		setTitle("Displays results from machine learning algorithms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	
		buildPanelButton();
		buildPanelSelection();	
		buildPanelTextArea();		//create panel to display results
		buildSample();		//inserting a panel for add sample	
		setPanel();
		
		setLayout(new BorderLayout());
		panelNorth =  new JPanel(new BorderLayout());
		panelTop = new JPanel(new BorderLayout());
		
		panelNorth.add(new TrainModel(), BorderLayout.WEST);
		panelNorth.add(new TestModel(), BorderLayout.EAST);
		panelTop.add(panelNorth, BorderLayout.NORTH);
		add(panelTop, BorderLayout.NORTH);
		
		panelEquator =  new JPanel(new BorderLayout());
		panelMiddle = new JPanel(new BorderLayout());

		//panelEquator.add(displayAddSample, BorderLayout.WEST);
		panelEquator.add(new DisplayAddSample(), BorderLayout.WEST);
		panelEquator.add(panelPredict, BorderLayout.CENTER);
		
		panelMiddle.add(panelEquator, BorderLayout.CENTER);
		add(panelMiddle, BorderLayout.CENTER);

		add(panelRadioButtons, BorderLayout.SOUTH);		

		pack();	
		revalidate();
		repaint();
		setVisible(true);
	}
	
	
	//method to provide the option to add sample 
	private void buildSample()
	{	displayAddSample = new DisplayAddSample();	
	}
	
	//method to make evaluate button listener
	private void buildPanelTextArea() 
	{	panelResultDisplay = new JPanel();
		buttonEvaluate.addActionListener(new buttonEvaluateListener());
		buttonpredict.addActionListener(new buttonPredictListener());
	}

	//method to build the panel that holds predict value
	private void setPanel()
	{	panelPredict = new JPanel();
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Predicted value");
		title.setTitleJustification(TitledBorder.CENTER);
		panelPredict.setBorder(title);
		setVisible(true);
	}
	
	//action listener for the button used to make prediction
	private class buttonPredictListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(choiceTree.isSelected() )
			{	displayPredictTable();
				
				if(predict.predictDTree().equals("2")){
					textAreaPredict.setText("Does not have diabetes");
					
				}
				else{
					textAreaPredict.setText("Has diabetes");
				}
			}
			else if(choiceC45Algo.isSelected() ){
				displayPredictTable();
				if(predict.predictNN().equals("2")){
					textAreaPredict.setText("Does not have diabetes");
				}
				else{
					textAreaPredict.setText("Has diabetes");
				}
			}
			else if (choiceC45Tree.isSelected()) {
				displayPredictTable();
				if(predict.predictC45Tree().equals("2")){
					textAreaPredict.setText("Does not have diabetes");	
				}
				else if (predict.predictC45Tree().equals("1")) {
					textAreaPredict.setText("Has diabetes");
				}
			}
		}
	}
	
	//make table to display prediction
	private void displayPredictTable(){
		textAreaPredict = new JTextArea(10,10);
		textAreaPredict.setEditable(false);
		textAreaPredict.setLineWrap(true);
		panelPredict.add(textAreaPredict);
		setPanel();
		revalidate();
		repaint();
		setVisible(true);
	}

	//action listener for the button to evaluate samples
	private class buttonEvaluateListener implements ActionListener{	
		public void actionPerformed(ActionEvent e)
		{	
			if(choiceTree.isSelected() )
			{	panelEquator.add(new PrepareTableTree(), BorderLayout.EAST);
				displayTable();
			}
			else if(choiceC45Algo.isSelected() )
			{	panelEquator.add(new PrepareTableNN(), BorderLayout.EAST);
				displayTable();
			}
			else
			{	panelEquator.add(new PrepareTableC45Tree(), BorderLayout.EAST);
				displayTable();
			}
		}
	}
	
	//method to make table to display results
	private void displayTable(){
		panelEquator.add(displayAddSample, BorderLayout.WEST);
		panelMiddle.add(panelEquator, BorderLayout.CENTER);
		add(panelMiddle, BorderLayout.CENTER);
		add(panelRadioButtons, BorderLayout.SOUTH);			

		setVisible(true);
	}
	
	//method to build panel that holds options for users
	private void buildPanelSelection()
	{	panelRadioButtons = new JPanel();
		choiceC45Tree = new JRadioButton("use C45Tree", true);
		choiceTree = new JRadioButton("use Decision Tree");		
		buttonEvaluate = new JButton("Evaluate");
		buttonpredict = new JButton("Predict");
		
		buttonGroup = new ButtonGroup();
	    buttonGroup.add(choiceC45Tree );  
	    buttonGroup.add(choiceC45Algo );
	    buttonGroup.add(choiceTree );
		buttonGroup.add(buttonEvaluate);
		buttonGroup.add(buttonpredict);

		panelRadioButtons.add(choiceC45Tree);
		panelRadioButtons.add(choiceC45Algo);
		panelRadioButtons.add(choiceTree);
		panelRadioButtons.add(buttonEvaluate);
		panelRadioButtons.add(buttonpredict);
		panelRadioButtons.add(buttonExit);
	}

	//method to build panel to hold buttons
	private void buildPanelButton()
	{	panelHoldJButtons = new JPanel();
		buttonExit = new JButton("Exit");
		buttonExit.addActionListener(new ExitButtonListener());	
	}
	
	//action listener for exit button
	private class ExitButtonListener implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	//end program
			System.exit(0);
		}
	}
}