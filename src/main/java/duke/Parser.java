package duke;

import command.Command;
import command.DeadlineCommand;
import command.DeleteCommand;
import command.EventCommand;
import command.ExitCommand;
import command.ListCommand;
import command.MarkCommand;
import command.TodoCommand;
import command.UnmarkCommand;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Parser {
    public static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
    public static Command parse(String input) throws DukeException {
        if (input.equals("list")) {
            return new ListCommand();
        }
        if (input.equals("bye")) {
            return new ExitCommand();
        } else {
            String[] details = input.split(" ", 2);
            String commandName = details[0];
            switch (commandName) {
                case MarkCommand.COMMAND_WORD:
                    return prepareMark(details);

                case UnmarkCommand.COMMAND_WORD:
                    return prepareUnmark(details);

                case DeleteCommand.COMMAND_WORD:
                    return prepareDelete(details);

                case TodoCommand.COMMAND_WORD:
                    return prepareTodo(details);

                case DeadlineCommand.COMMAND_WORD:
                    return prepareDeadline(details);

                case EventCommand.COMMAND_WORD:
                    return prepareEvent(details);

                default:
                    throw new DukeException("Sorry! I do not recognise this command");
            }
        }
    }

    private static MarkCommand prepareMark(String[] details) throws DukeException {
        if (details.length < 2 || details[1].trim().isEmpty()) {
            throw new DukeException("Invalid command! " +
                    "Please include the index of the task you wish to mark");
        }
        int markTaskId = Integer.parseInt(details[1]) - 1;
        return new MarkCommand(markTaskId);
    }

    private static UnmarkCommand prepareUnmark(String[] details) throws DukeException {
        if (details.length < 2 || details[1].trim().isEmpty()) {
            throw new DukeException("Invalid command! " +
                    "Please include the index of the task you wish to unmark");
        }
        int unmarkTaskId = Integer.parseInt(details[1]) - 1;
        return new UnmarkCommand(unmarkTaskId);
    }

    private static DeleteCommand prepareDelete(String[] details) throws DukeException {
        if (details.length < 2 || details[1].trim().isEmpty()) {
            throw new DukeException("Invalid command! " +
                    "Please include the index of the task you wish to delete");
        }
        int deleteTaskId = Integer.parseInt(details[1]) - 1;
        return new DeleteCommand(deleteTaskId);
    }

    private static TodoCommand prepareTodo(String[] details) throws DukeException {
        // user input only has the command eg "todo"
        if (details.length < 2 || details[1].trim().isEmpty()) {
            throw new DukeException("Invalid command! Please include details of this task");
        }
        String todoDesc = details[1].trim();
        return new TodoCommand(todoDesc);
    }

    private static DeadlineCommand prepareDeadline(String[] details) throws DukeException {
        // user input only has the command eg "deadline"
        if (details.length < 2 || details[1].trim().isEmpty()) {
            throw new DukeException("Invalid command! Please include details of this task");
        }
        String[] deadline = details[1].split("/by", 2);
        if (deadline.length < 2 || deadline[1].trim().isEmpty()) { // user input does not have /by
            throw new DukeException("Invalid command! Please include the deadline of this task");
        }
        if (deadline[0].split(" ", 2).length < 2) {
            throw new DukeException("Invalid command! Please include details of this task");
        }
        LocalDateTime by = LocalDateTime.parse(deadline[1].trim(), inputFormatter);
        return new DeadlineCommand(deadline[0].trim(), by);
    }

    private static EventCommand prepareEvent(String[] details) throws DukeException {
        // user input only has the command eg "event"
        if (details.length < 2 || details[1].trim().isEmpty()) {
            throw new DukeException("Invalid command! Please include details of this task");
        }
        String[] eventDetails = details[1].split("/from", 2);
        // user input does not include /from
        if (eventDetails.length < 2 || eventDetails[1].trim().isEmpty()) {
            throw new DukeException("Invalid command! Please include when the event starts");
        }
        // user input does not include /from
        if (eventDetails[0].split(" ", 2).length < 2) {
            throw new DukeException("Invalid command! Please include details of this task");
        }
        String[] eventTimings = eventDetails[1].split("/to", 2);
        // user input does not include /to
        if (eventTimings.length < 2 || eventTimings[1].trim().isEmpty()) {
            throw new DukeException("Invalid command! Please include when the event ends");
        }
        LocalDateTime from = LocalDateTime.parse(eventTimings[0].trim(), inputFormatter);
        LocalDateTime to = LocalDateTime.parse(eventTimings[1].trim(), inputFormatter);
        return new EventCommand(eventDetails[0].trim(), from, to);
    }
}
