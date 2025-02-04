package io.github.dsheirer.gui.channelizer;

import io.github.dsheirer.buffer.FloatNativeBuffer;
import io.github.dsheirer.buffer.INativeBuffer;
import io.github.dsheirer.dsp.filter.FilterFactory;
import io.github.dsheirer.dsp.filter.channelizer.TwoChannelSynthesizerM2;
import io.github.dsheirer.dsp.filter.design.FilterDesignException;
import io.github.dsheirer.dsp.oscillator.FS4DownConverter;
import io.github.dsheirer.dsp.oscillator.IComplexOscillator;
import io.github.dsheirer.dsp.oscillator.OscillatorFactory;
import io.github.dsheirer.sample.Listener;
import io.github.dsheirer.sample.complex.ComplexSamples;
import io.github.dsheirer.settings.SettingsManager;
import io.github.dsheirer.spectrum.ComplexDftProcessor;
import io.github.dsheirer.spectrum.DFTSize;
import io.github.dsheirer.spectrum.SpectrumPanel;
import io.github.dsheirer.spectrum.converter.ComplexDecibelConverter;
import io.github.dsheirer.util.ThreadPool;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SynthesizerViewerFX extends Application {
	private final static Logger mLog = LoggerFactory.getLogger(SynthesizerViewerFX.class);
	
	private static final int CHANNEL_BANDWIDTH = 12500;
	private static final int CHANNEL_SAMPLE_RATE = 25000;
	private static final int CHANNEL_FFT_FRAME_RATE = 20; // frames per second
	private static final int DATA_GENERATOR_FRAME_RATE = 50; // frames per second
	
	private SettingsManager mSettingsManager = new SettingsManager();
	private PrimarySpectrumPanel mPrimarySpectrumPanel;
	private ChannelPanel mChannel1Panel;
	private ChannelPanel mChannel2Panel;
	private ChannelControlPanel mChannel1ControlPanel;
	private ChannelControlPanel mChannel2ControlPanel;
	private DFTSize mMainPanelDFTSize = DFTSize.FFT08192;
	private DFTSize mChannelPanelDFTSize = DFTSize.FFT08192;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Polyphase Synthesizer Viewer");
		primaryStage.setWidth(500);
		primaryStage.setHeight(400);
		
		GridPane root = new GridPane();
		root.add(getPrimaryPanel(), 0, 0);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		startDataGeneration();
	}
	
	private VBox getPrimaryPanel() {
		VBox primaryPanel = new VBox();
		primaryPanel.getChildren().addAll(getSpectrumPanel(), getChannel1Panel(), getChannel2Panel());
		return primaryPanel;
	}
	
	private PrimarySpectrumPanel getSpectrumPanel() {
		if (mPrimarySpectrumPanel == null) {
			mPrimarySpectrumPanel = new PrimarySpectrumPanel(mSettingsManager);
			mPrimarySpectrumPanel.setPrefSize(500, 200);
			mPrimarySpectrumPanel.setDFTSize(mMainPanelDFTSize);
		}
		return mPrimarySpectrumPanel;
	}
	
	private ChannelPanel getChannel1Panel() {
		if (mChannel1Panel == null) {
			mChannel1Panel = new ChannelPanel(mSettingsManager, getChannel1ControlPanel());
			mChannel1Panel.setPrefSize(250, 200);
			mChannel1Panel.setDFTSize(mChannelPanelDFTSize);
		}
		return mChannel1Panel;
	}
	
	private ChannelPanel getChannel2Panel() {
		if (mChannel2Panel == null) {
			mChannel2Panel = new ChannelPanel(mSettingsManager, getChannel2ControlPanel());
			mChannel2Panel.setPrefSize(250, 200);
			mChannel2Panel.setDFTSize(mChannelPanelDFTSize);
		}
		return mChannel2Panel;
	}
	
	private ChannelControlPanel getChannel1ControlPanel() {
		if (mChannel1ControlPanel == null) {
			mChannel1ControlPanel = new ChannelControlPanel();
		}
		return mChannel1ControlPanel;
	}
	
	private ChannelControlPanel getChannel2ControlPanel() {
		if (mChannel2ControlPanel == null) {
			mChannel2ControlPanel = new ChannelControlPanel();
		}
		return mChannel2ControlPanel;
	}
	
	private void startDataGeneration() {
		ThreadPool.SCHEDULED.scheduleAtFixedRate(new DataGenerationManager(), 0, 1000 / DATA_GENERATOR_FRAME_RATE, TimeUnit.MILLISECONDS);
	}
	
	public class PrimarySpectrumPanel extends VBox implements Listener<INativeBuffer> {
		private ComplexDftProcessor mComplexDftProcessor = new ComplexDftProcessor();
		private ComplexDecibelConverter mComplexDecibelConverter = new ComplexDecibelConverter();
		private SpectrumPanel mSpectrumPanel;
		
		public PrimarySpectrumPanel(SettingsManager settingsManager) {
			mSpectrumPanel = new SpectrumPanel(settingsManager);
			mSpectrumPanel.setSampleSize(16);
			getChildren().add(mSpectrumPanel);
			
			mComplexDftProcessor.addConverter(mComplexDecibelConverter);
			mComplexDftProcessor.setFrameRate(CHANNEL_FFT_FRAME_RATE);
			mComplexDecibelConverter.addListener(mSpectrumPanel);
		}
		
		public void setDFTSize(DFTSize dftSize) {
			mComplexDftProcessor.setDFTSize(dftSize);
		}
		
		@Override
		public void receive(INativeBuffer nativeBuffer) {
			mComplexDftProcessor.receive(nativeBuffer);
		}
	}
	
	public class ChannelPanel extends VBox implements Listener<INativeBuffer> {
		private ComplexDftProcessor mComplexDftProcessor = new ComplexDftProcessor();
		private ComplexDecibelConverter mComplexDecibelConverter = new ComplexDecibelConverter();
		private SpectrumPanel mSpectrumPanel;
		
		public ChannelPanel(SettingsManager settingsManager, ChannelControlPanel channelControlPanel) {
			mSpectrumPanel = new SpectrumPanel(settingsManager);
			mSpectrumPanel.setSampleSize(16);
			getChildren().addAll(mSpectrumPanel, channelControlPanel);
			
			mComplexDftProcessor.addConverter(mComplexDecibelConverter);
			mComplexDftProcessor.setFrameRate(CHANNEL_FFT_FRAME_RATE);
			mComplexDecibelConverter.addListener(mSpectrumPanel);
		}
		
		public void setDFTSize(DFTSize dftSize) {
			mComplexDftProcessor.setDFTSize(dftSize);
		}
		
		@Override
		public void receive(INativeBuffer nativeBuffer) {
			mComplexDftProcessor.receive(nativeBuffer);
		}
	}
	
	public class ChannelControlPanel extends VBox {
		private static final int MIN_FREQUENCY = -6250;
		private static final int MAX_FREQUENCY = 6250;
		private static final int DEFAULT_FREQUENCY = 50;
		
		private IComplexOscillator mOscillator = OscillatorFactory.getComplexOscillator(DEFAULT_FREQUENCY, CHANNEL_SAMPLE_RATE);
		
		public ChannelControlPanel() {
			Label label = new Label("Tone:");
			Spinner<Integer> spinner = new Spinner<>();
			SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_FREQUENCY, MAX_FREQUENCY, DEFAULT_FREQUENCY, 100);
			spinner.setValueFactory(valueFactory);
			
			valueFactory.valueProperty().addListener((obs, oldValue, newValue) -> mOscillator.setFrequency(newValue));
			
			getChildren().addAll(label, spinner, new Label("Hz"));
		}
		
		public IComplexOscillator getOscillator() {
			return mOscillator;
		}
	}
	
	public class DataGenerationManager implements Runnable {
		private TwoChannelSynthesizerM2 mSynthesizer;
		private FS4DownConverter mFS4DownConverter = new FS4DownConverter();
		private int mSamplesPerCycle = CHANNEL_SAMPLE_RATE / DATA_GENERATOR_FRAME_RATE;
		
		public DataGenerationManager() {
			try {
				float[] taps = FilterFactory.getSincM2Synthesizer(25000.0, 12500.0, 2, 12);
				mSynthesizer = new TwoChannelSynthesizerM2(taps);
			} catch (FilterDesignException fde) {
				mLog.error("Filter design error", fde);
			}
		}
		
		@Override
		public void run() {
			ComplexSamples channel1Buffer = getChannel1ControlPanel().getOscillator().generateComplexSamples(mSamplesPerCycle, 0L);
			ComplexSamples channel2Buffer = getChannel2ControlPanel().getOscillator().generateComplexSamples(mSamplesPerCycle, 0L);
			
			ComplexSamples synthesizedBuffer = mSynthesizer.process(channel1Buffer, channel2Buffer);
			getChannel1Panel().receive(new FloatNativeBuffer(channel1Buffer));
			getChannel2Panel().receive(new FloatNativeBuffer(channel2Buffer));
			getSpectrumPanel().receive(new FloatNativeBuffer(synthesizedBuffer));
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}