package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;

import simulator.factories.*;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;

import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = "gui";
	private static Factory<Event> _eventsFactory = null;
	private static Integer _ticks = _timeLimitDefaultValue;
	private static Boolean isGuiMode = false;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseModeOption(line);
			parseInFileOption(line);
			parseHelpOption(line, cmdLineOptions);
			parseOutFileOption(line);
			parseTicksOption(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}


	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator�s main loop (default\r\n" + "value is 10).").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Shows interface").build());
		return cmdLineOptions;
	}

	private static void parseModeOption(CommandLine line) {
		_mode = line.getOptionValue("m");
		if (line.hasOption("m") && _mode.equals("gui")) {
			isGuiMode = true;
		}
	}
	
	private static void parseTicksOption(CommandLine line) {
		String aux = line.getOptionValue("t");
		if (aux == null)
			_ticks = _timeLimitDefaultValue;
		else 
			_ticks = Integer.parseInt(aux);
	}
	
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (!isGuiMode && _inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if (!isGuiMode)
			_outFile = line.getOptionValue("o");
	}

	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		ArrayList<Builder<Event>> eventsBu = new ArrayList<>();
		eventsBu.add(new NewCityRoadEventBuilder());
		eventsBu.add(new NewInterCityRoadEventBuilder());
		eventsBu.add(new NewVehicleEventBuilder());
		eventsBu.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
		eventsBu.add(new SetWeatherEventBuilder());
		eventsBu.add(new SetContClassEventBuilder());
		_eventsFactory = new BuilderBasedFactory<Event>(eventsBu);
	}

	private static void startBatchMode() throws IOException {
		TrafficSimulator simulator = new TrafficSimulator();
		Controller control = new Controller(simulator, _eventsFactory);
		OutputStream o;
		try(InputStream in = new FileInputStream(new File(_inFile));){
			control.loadEvents(in);
			if(_outFile == null)
				o = System.out;
			else
				o = new FileOutputStream(_outFile);
			control.run(_ticks, o);
			
			in.close();
			o.close();
		}catch(FileNotFoundException e) {
			throw new IOException("Input file not found");
		}
	}
	
	private static void startGUIMode() throws IOException{
		TrafficSimulator simulator = new TrafficSimulator();
		Controller control = new Controller(simulator, _eventsFactory);
		OutputStream o;
		if(_inFile != null) {
			try(InputStream in = new FileInputStream(new File(_inFile));){
				control.loadEvents(in);
			
				in.close();
			
			}catch(FileNotFoundException e) {
				throw new IOException("Input file not found");
			}
		}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new MainWindow(control);
				}
			});
		
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if (!isGuiMode)
			startBatchMode();
		else
			startGUIMode();
	}

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}