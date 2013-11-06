package pl.psnc.synat.a12.aletheia;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public final class CLIUtils {

    public interface Command {

        public String name();


        public boolean isHelp();
    }


    private CLIUtils() {
        // empty
    }


    public static void handleParameterException(JCommander engine, ParameterException e) {
        StringBuilder sb = new StringBuilder(e.getMessage());
        String commandName = engine.getParsedCommand();

        if (commandName != null) {
            String desc = engine.getCommandDescription(commandName);
            sb.append(" (\"").append(desc).append("\")");
        }
        System.err.println(sb.toString());
        engine.usage();
    }


    public static JCommander createEngine(String name, Command main, Command... commands) {
        JCommander engine = new JCommander(main);

        engine.setProgramName(name);

        for (Command command : commands) {
            engine.addCommand(command.name(), command);
        }
        return engine;
    }


    public static void checkHelp(JCommander engine, Command arguments) {
        if (arguments.isHelp()) {
            engine.usage();
            System.exit(0);
        }
    }
}
