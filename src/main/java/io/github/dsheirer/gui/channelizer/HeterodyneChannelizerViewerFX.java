package io.github.dsheirer.gui.channelizer;

import io.github.dsheirer.buffer.FloatNativeBuffer;
import io.github.dsheirer.buffer.INativeBuffer;
import io.github.dsheirer.sample.Listener;
import io.github.dsheirer.sample.complex.ComplexSamples;
import io.github.dsheirer.settings.SettingsManager;
import io.github.dsheirer.source.ISourceEventProcessor;
import io.github.dsheirer.source.SourceEvent;
import io.github.dsheirer.source.SourceException;
import io.github.dsheirer.source.tuner.LoggingTunerErrorListener;
import io.github.dsheirer.source.tuner.Tuner;
import io.github.dsheirer.source.tuner.channel.ChannelSpecification;
import io.github.dsheirer.source.tuner.channel.TunerChannel;
import io.github.dsheirer.source.tuner.channel.TunerChannelSource;
import io.github.dsheirer.source.tuner.test.TestTuner;
import io.github.dsheirer.spectrum.ComplexDftProcessor;
import io.github.dsheirer.spectrum.DFTSize;
import io.github.dsheirer.spectrum.SpectrumPanel;
import io.github.dsheirer.spectrum.converter.ComplexDecibelConverter;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeterodyneChannelizerViewer extends Application
{
    private final static Logger mLog = LoggerFactory.getLogger(HeterodyneChannelizerViewer.class);
    
    private static final int CHANNEL_BANDWIDTH = 12500;
    private static final int CHANNEL_FFT_FRAME_RATE = 20;
    
    private SettingsManager mSettingsManager = new SettingsManager();
    private VBox mPrimaryPanel;
    private GridPane mControlPanel;
    private Label mToneFrequencyLabel;
    private PrimarySpectrumPanel mPrimarySpectrumPanel;
    private ChannelArrayPanel mChannelPanel;
    private DiscreteIndexChannelArrayPanel mDiscreteIndexChannelPanel;
    private int mChannelCount;
    private int mChannelsPerRow;
    private long mBaseFrequency = 100000000;  //100 MHz
    private DFTSize mMainPanelDFTSize = DFTSize.FFT04096;
    private DFTSize mChannelPanelDFTSize = DFTSize.FFT04096;
    private TestTuner mTestTuner;
    
    /**
     * GUI Test utility for researching channelizers.
     */
    public HeterodyneChannelizerViewer()
    {
        mTestTuner = new TestTuner(new LoggingTunerErrorListener());
        mChannelCount = 5;
        mChannelsPerRow = 5;
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Heterodyne Channelizer Viewer");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setScene(new Scene(getPrimaryPanel()));
        primaryStage.show();
    }
    
    private VBox getPrimaryPanel()
    {
        if(mPrimaryPanel == null)
        {
            mPrimaryPanel = new VBox();
            mPrimaryPanel.setPadding(new Insets(10));
            mPrimaryPanel.setSpacing(10);
            mPrimaryPanel.getChildren().addAll(getSpectrumPanel(), getControlPanel(), getChannelArrayPanel());
        }
        
        return mPrimaryPanel;
    }
    
    private PrimarySpectrumPanel getSpectrumPanel()
    {
        if(mPrimarySpectrumPanel == null)
        {
            mPrimarySpectrumPanel = new PrimarySpectrumPanel(mSettingsManager,
                    mTestTuner.getTunerController().getSampleRate());
            mPrimarySpectrumPanel.setPrefSize(1200, 200);
            mPrimarySpectrumPanel.setDFTSize(mMainPanelDFTSize);
            mTestTuner.getTunerController().addBufferListener(mPrimarySpectrumPanel);
        }
        
        return mPrimarySpectrumPanel;
    }
    
    private GridPane getControlPanel()
    {
        if(mControlPanel == null)
        {
            mControlPanel = new GridPane();
            mControlPanel.setHgap(10);
            mControlPanel.setVgap(10);
            
            mControlPanel.add(new Label("Tone:"), 0, 0);
            long minimumFrequency = -(long)mTestTuner.getTunerController().getSampleRate() / 2 + 1;
            long maximumFrequency = (long)mTestTuner.getTunerController().getSampleRate() / 2 - 1;
            long toneFrequency = 0;
            
            SpinnerValueFactory<Long> valueFactory = new SpinnerValueFactory<Long>() {
                {
                    setValue(toneFrequency);
                }
                
                @Override
                public void decrement(int steps) {
                    setValue(getValue() - steps * 100);
                }
                
                @Override
                public void increment(int steps) {
                    setValue(getValue() + steps * 100);
                }
            };
            
            Spinner<Long> spinner = new Spinner<>(valueFactory);
            valueFactory.valueProperty().addListener((obs, oldValue, newValue) -> {
                mTestTuner.getTunerController().setToneFrequency(newValue);
                mToneFrequencyLabel.setText(String.valueOf(getToneFrequency()));
            });
            
            mControlPanel.add(spinner, 1, 0);
            mControlPanel.add(new Label("Hz"), 2, 0);
            
            mControlPanel.add(new Label("Frequency:"), 3, 0);
            mToneFrequencyLabel = new Label(String.valueOf(getToneFrequency()));
            mControlPanel.add(mToneFrequencyLabel, 4, 0);
            
            mControlPanel.add(new Label("Channels: " + mChannelCount), 5, 0);
        }
        
        return mControlPanel;
    }
    
    private long getToneFrequency()
    {
        return mTestTuner.getTunerController().getFrequency() + mTestTuner.getTunerController().getToneFrequency();
    }
    
    private ChannelArrayPanel getChannelArrayPanel()
    {
        if(mChannelPanel == null)
        {
            mChannelPanel = new ChannelArrayPanel();
        }
        
        return mChannelPanel;
    }
    
    private DiscreteIndexChannelArrayPanel getDiscreteIndexChannelPanel()
    {
        if(mDiscreteIndexChannelPanel == null)
        {
            mDiscreteIndexChannelPanel = new DiscreteIndexChannelArrayPanel();
        }
        
        return mDiscreteIndexChannelPanel;
    }
    
    public class ChannelArrayPanel extends GridPane
    {
        private final Logger mLog = LoggerFactory.getLogger(ChannelArrayPanel.class);
        
        public ChannelArrayPanel()
        {
            int bufferSize = CHANNEL_BANDWIDTH / CHANNEL_FFT_FRAME_RATE;
            if(bufferSize % 2 == 1)
            {
                bufferSize++;
            }
            
            init();
        }
        
        private void init()
        {
            setHgap(10);
            setVgap(10);
            
            double spectralBandwidth = mTestTuner.getTunerController().getSampleRate();
            double halfSpectralBandwidth = spectralBandwidth / 2.0;
            
            int channelToLog = -1;
            
            long baseFrequency = mBaseFrequency + (CHANNEL_BANDWIDTH / 2);
            
            for(int x = 0; x < mChannelCount; x++)
            {
                long frequency = baseFrequency + (x * CHANNEL_BANDWIDTH);
                
                mLog.debug("Channel " + x + "/" + mChannelCount + " Frequency: " + frequency);
                
                ChannelPanel channelPanel = new ChannelPanel(mSettingsManager, CHANNEL_BANDWIDTH * 2, frequency, CHANNEL_BANDWIDTH, (x == channelToLog));
                channelPanel.setDFTSize(mChannelPanelDFTSize);
                
                if(x % mChannelsPerRow == mChannelsPerRow - 1)
                {
                    add(channelPanel, x % mChannelsPerRow, x / mChannelsPerRow);
                }
                else
                {
                    add(channelPanel, x % mChannelsPerRow, x / mChannelsPerRow);
                }
            }
        }
    }
    
    public class DiscreteIndexChannelArrayPanel extends GridPane
    {
        public DiscreteIndexChannelArrayPanel()
        {
            int bufferSize = CHANNEL_BANDWIDTH / CHANNEL_FFT_FRAME_RATE;
            if(bufferSize % 2 == 1)
            {
                bufferSize++;
            }
            
            init();
        }
        
        private void init()
        {
            setHgap(10);
            setVgap(10);
            
            ChannelSpecification channelSpecification = new ChannelSpecification(25000.0, 12500, 6000.0, 6250.0);
            for(int x = 0; x < mChannelCount; x++)
            {
                TunerChannel tunerChannel = new TunerChannel(100000000, 12500);
                TunerChannelSource source = mTestTuner.getChannelSourceManager().getSource(tunerChannel,
                        channelSpecification, "test");
                DiscreteChannelPanel channelPanel = new DiscreteChannelPanel(mSettingsManager, source, x);
                channelPanel.setDFTSize(mChannelPanelDFTSize);
                
                mLog.debug("Testing Channel [" + x + "] is set to [" + source.getTunerChannel().getFrequency() + "]");
                
                if(x % mChannelsPerRow == mChannelsPerRow - 1)
                {
                    add(channelPanel, x % mChannelsPerRow, x / mChannelsPerRow);
                }
                else
                {
                    add(channelPanel, x % mChannelsPerRow, x / mChannelsPerRow);
                }
            }
        }
    }
    
    public class PrimarySpectrumPanel extends VBox implements Listener<INativeBuffer>, ISourceEventProcessor
    {
        private ComplexDftProcessor mComplexDftProcessor = new ComplexDftProcessor();
        private ComplexDecibelConverter mComplexDecibelConverter = new ComplexDecibelConverter();
        private SpectrumPanel mSpectrumPanel;
        
        public PrimarySpectrumPanel(SettingsManager settingsManager, double sampleRate)
        {
            setPadding(new Insets(10));
            setSpacing(10);
            mSpectrumPanel = new SpectrumPanel(settingsManager);
            mSpectrumPanel.setSampleSize(28);
            getChildren().add(mSpectrumPanel);
            
            mComplexDftProcessor.addConverter(mComplexDecibelConverter);
            mComplexDecibelConverter.addListener(mSpectrumPanel);
        }
        
        public void setDFTSize(DFTSize dftSize)
        {
            mComplexDftProcessor.setDFTSize(dftSize);
        }
        
        @Override
        public void receive(INativeBuffer nativeBuffer)
        {
            mComplexDftProcessor.receive(nativeBuffer);
        }
        
        @Override
        public void process(SourceEvent event) throws SourceException
        {
            mLog.debug("Source Event!  Add handler support for this to channelizer viewer");
        }
    }
    
    public class ChannelPanel extends VBox implements Listener<ComplexSamples>, ISourceEventProcessor
    {
        private TunerChannelSource mSource;
        private ComplexDftProcessor mComplexDftProcessor = new ComplexDftProcessor();
        private ComplexDecibelConverter mComplexDecibelConverter = new ComplexDecibelConverter();
        private SpectrumPanel mSpectrumPanel;
        private ToggleButton mLoggingButton;
        private boolean mLoggingEnabled;
        
        public ChannelPanel(SettingsManager settingsManager, double sampleRate, long frequency, int bandwidth, boolean enableLogging)
        {
            setPadding(new Insets(10));
            setSpacing(10);
            mSpectrumPanel = new SpectrumPanel(settingsManager);
            mSpectrumPanel.setSampleSize(32);
            getChildren().add(mSpectrumPanel);
            
            mComplexDftProcessor.addConverter(mComplexDecibelConverter);
            mComplexDecibelConverter.addListener(mSpectrumPanel);
            
            TunerChannel tunerChannel = new TunerChannel(frequency, bandwidth);
            ChannelSpecification channelSpecification = new ChannelSpecification(25000.0, 12500, 6000.0, 6250.0);
            mSource = mTestTuner.getChannelSourceManager().getSource(tunerChannel, channelSpecification, "test");
            
            if(mSource != null)
            {
                mSource.setListener(complexSamples -> mComplexDftProcessor.receive(new FloatNativeBuffer(complexSamples.toInterleaved())));
                
                mSource.start();
            }
            else
            {
                mLog.error("Couldn't get a source from the tuner for frequency: " + frequency);
            }
            
            if(mSource != null)
            {
                getChildren().add(new Label("Center:" + frequency));
            }
            else
            {
                getChildren().add(new Label("NO SRC:" + frequency));
            }
            
            mLoggingButton = new ToggleButton("Logging");
            mLoggingButton.setOnAction(e -> mLoggingEnabled = mLoggingButton.isSelected());
//            getChildren().add(mLoggingButton);
        }
        
        public TunerChannelSource getSource()
        {
            return mSource;
        }
        
        public void setDFTSize(DFTSize dftSize)
        {
            mComplexDftProcessor.setDFTSize(dftSize);
        }
        
        @Override
        public void receive(ComplexSamples complexSamples)
        {
            mComplexDftProcessor.receive(new FloatNativeBuffer(complexSamples.toInterleaved()));
        }
        
        @Override
        public void process(SourceEvent event) throws SourceException
        {
            mLog.debug("Source Event!  Add handler support for this to channelizer viewer");
        }
    }
    
    public class DiscreteChannelPanel extends VBox implements Listener<ComplexSamples>, ISourceEventProcessor
    {
        private final Logger mLog = LoggerFactory.getLogger(DiscreteChannelPanel.class);
        
        private TunerChannelSource mSource;
        private ComplexDftProcessor mComplexDftProcessor = new ComplexDftProcessor();
        private ComplexDecibelConverter mComplexDecibelConverter = new ComplexDecibelConverter();
        private SpectrumPanel mSpectrumPanel;
        private ToggleButton mLoggingButton;
        private boolean mLoggingEnabled;
        
        public DiscreteChannelPanel(SettingsManager settingsManager, TunerChannelSource source, int index)
        {
            mSource = source;
            setPadding(new Insets(10));
            setSpacing(10);
            mSpectrumPanel = new SpectrumPanel(settingsManager);
            mSpectrumPanel.setSampleSize(32);
            getChildren().add(mSpectrumPanel);
            getChildren().add(new Label("Index:" + index));
            
            mLoggingButton = new ToggleButton("Logging");
            mLoggingButton.setOnAction(e -> mLoggingEnabled = mLoggingButton.isSelected());
//            getChildren().add(mLoggingButton);
            
            mComplexDftProcessor.addConverter(mComplexDecibelConverter);
            mComplexDecibelConverter.addListener(mSpectrumPanel);
            
            if(mSource != null)
            {
                mSource.setListener(new Listener<ComplexSamples>()
                {
                    @Override
                    public void receive(ComplexSamples complexSamples)
                    {
                        if(mLoggingEnabled)
                        {
                            mLog.debug("Samples:" + Arrays.toString(complexSamples.toInterleaved().samples()));
                        }
                        
                        mComplexDftProcessor.receive(new FloatNativeBuffer(complexSamples.toInterleaved()));
                    }
                });
                
                mSource.start();
            }
            else
            {
                mLog.error("Couldn't get a source from the tuner for index: " + index);
            }
        }
        
        public TunerChannelSource getSource()
        {
            return mSource;
        }
        
        public void setDFTSize(DFTSize dftSize)
        {
            mComplexDftProcessor.setDFTSize(dftSize);
        }
        
        @Override
        public void receive(ComplexSamples complexSamples)
        {
            mComplexDftProcessor.receive(new FloatNativeBuffer(complexSamples.toInterleaved()));
        }
        
        @Override
        public void process(SourceEvent event) throws SourceException
        {
            mLog.debug("Source Event!  Add handler support for this to channelizer viewer");
        }
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}