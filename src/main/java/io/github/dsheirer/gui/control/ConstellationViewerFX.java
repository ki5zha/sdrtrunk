package io.github.dsheirer.gui.control;

import io.github.dsheirer.buffer.CircularBuffer;
import io.github.dsheirer.sample.Listener;
import io.github.dsheirer.sample.complex.Complex;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.apache.commons.math3.util.FastMath;

import java.util.List;

public class ConstellationViewerFX extends Pane implements Listener<Complex> {
	private final int mSampleRate;
	private final int mSymbolRate;
	private final float mSamplesPerSymbol;
	private float mCounter = 0;
	private float mOffset = 0;
	
	private final CircularBuffer<Complex> mBuffer = new CircularBuffer<>(5000); // stores samples
	private Complex mPrevious = new Complex(1, 1);
	
	private final Canvas mCanvas; // canvas for visualization
	
	public ConstellationViewerFX(int sampleRate, int symbolRate) {
		mSampleRate = sampleRate;
		mSymbolRate = symbolRate;
		mSamplesPerSymbol = (float) mSampleRate / (float) mSymbolRate;
		
		mCanvas = new Canvas(400, 400); // set canvas size
		getChildren().add(mCanvas); // add canvas to this Pane
		
		initContextMenu(); // setup right-click menu
	}
	
	/**
	 * Initializes the right-click context menu.
	 */
	private void initContextMenu() {
		mCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				ContextMenu contextMenu = new ContextMenu();
				
				MenuItem timingOffsetSettings = new MenuItem("Adjust Timing Offset");
				timingOffsetSettings.setOnAction(e -> showTimingOffsetPopup());
				
				contextMenu.getItems().add(timingOffsetSettings);
				contextMenu.show(mCanvas, event.getScreenX(), event.getScreenY());
			}
		});
	}
	
	/**
	 * Displays a popup to adjust the timing offset using a slider.
	 */
	private void showTimingOffsetPopup() {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Adjust Timing Offset");
		
		Slider timingOffsetSlider = new Slider(0, mSamplesPerSymbol * 10, mOffset * 10);
		timingOffsetSlider.setMajorTickUnit(10);
		timingOffsetSlider.setMinorTickCount(5);
		timingOffsetSlider.setShowTickMarks(true);
		timingOffsetSlider.setShowTickLabels(true);
		
		// Display the current offset value
		Label currentOffsetLabel = new Label();
		currentOffsetLabel.setText(String.format("Current Offset: %.2f", mOffset));
		
		timingOffsetSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			mOffset = newValue.floatValue() / 10.0f;
			currentOffsetLabel.setText(String.format("Current Offset: %.2f", mOffset));
			drawSamples(); // Update the plot as the offset changes
		});
		
		VBox layout = new VBox(10, timingOffsetSlider, currentOffsetLabel);
		layout.setStyle("-fx-padding: 10; -fx-alignment: center;");
		
		popupStage.setScene(new Scene(layout));
		popupStage.setWidth(300);
		popupStage.setHeight(200);
		popupStage.show();
	}
	
	/**
	 * Handles incoming `Complex` samples for the constellation.
	 */
	@Override
	public void receive(Complex sample) {
		mBuffer.receive(sample);
		mPrevious = sample; // Save the previous sample
		drawSamples(); // Update the canvas
	}
	
	/**
	 * Draws the constellation plot on the canvas.
	 */
	private void drawSamples() {
		GraphicsContext gc = mCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight()); // clear the canvas
		
		gc.setFill(Color.BLUE);
		
		List<Complex> samples = mBuffer.getElements();
		double centerX = mCanvas.getWidth() / 2.0;
		double centerY = mCanvas.getHeight() / 2.0;
		
		double scale = 100.0; // Adjust this value to fit data on the canvas
		
		mCounter = 0;
		
		for (Complex sample : samples) {
			if (mCounter > (mOffset + mSamplesPerSymbol)) {
				/**
				 * Multiply the current sample against the complex conjugate of the
				 * previous sample to derive the phase delta between the two samples.
				 */
				double i = (sample.inphase() * mPrevious.inphase()) -
						(sample.quadrature() * -mPrevious.quadrature());
				double q = (sample.quadrature() * mPrevious.inphase()) +
						(sample.inphase() * -mPrevious.quadrature());
				
				// Avoid divide by zero and calculate angle
				double angle = (i != 0) ? FastMath.atan(q / i) : 0.0;
				
				// Draw the sample as a small circle
				double x = centerX + (i * scale);
				double y = centerY - (q * scale);
				gc.fillOval(x, y, 4, 4); // draw a 4x4 dot at sample position
				
				mCounter -= mSamplesPerSymbol; // reset counter
			}
			mCounter++;
		}
	}
}