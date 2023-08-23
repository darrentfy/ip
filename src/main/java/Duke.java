import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    public static void printLine(String message) {
        System.out.println(" ────────────────────────────────────────\n"
                + message
                + "\n ────────────────────────────────────────");
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        String welcome = "  Hello! I'm Handsome\n"
                + "  What can I do for you?";
        String goodbye = "  Bye. Hope to see you again soon!";
        printLine(welcome);
        while (true) {
            String input = scan.nextLine();
            if (input.equals("bye")) {
                break;
            }
            if (input.equals("list")) {
                int index = 1;
                System.out.println(" ────────────────────────────────────────\n"
                        + "Here are the tasks in your list:");
                for (Task task : tasks) {
                    System.out.println(String.format(" %d. %s ", index, task.toString()));
                    index++;
                }
                System.out.println(" ────────────────────────────────────────");
            } else {
                try {
                    String[] substrings = input.split(" ", 2);
                    String command = substrings[0];
                    switch (command) {
                        case "mark":
                            if (substrings.length < 2) { // user input only has the command eg "mark"
                                throw new DukeException("Invalid command! " +
                                        "Please include the index of the task you wish to mark");
                            }
                            int markTaskId = Integer.parseInt(substrings[1]) - 1;
                            if (markTaskId >= 0 && markTaskId < tasks.size()) {
                                tasks.get(markTaskId).markAsDone();
                            } else { // user input is an integer bigger than size of task list
                                String message = tasks.size() > 0
                                        ? "No such task! Please enter a task ID between 1 and " + tasks.size()
                                        : "You have no tasks! Please add some tasks first";
                                throw new DukeException(message);
                            }
                            break;
                        case "unmark":
                            if (substrings.length < 2) { // user input only has the command eg "unmark"
                                throw new DukeException("Invalid command! " +
                                        "Please include the index of the task you wish to unmark");
                            }
                            int unmarkTaskId = Integer.parseInt(substrings[1]) - 1;
                            if (unmarkTaskId >= 0 && unmarkTaskId < tasks.size()) {
                                tasks.get(unmarkTaskId).markAsUndone();
                            } else { // user input is an integer bigger than size of task list
                                String message = tasks.size() > 0
                                        ? "No such task! Please enter a task ID between 1 and " + tasks.size()
                                        : "You have no tasks! Please add some tasks first";
                                throw new DukeException(message);
                            }
                            break;
                        case "delete":
                            if (substrings.length < 2) { // user input only has the command eg "delete"
                                throw new DukeException("Invalid command! " +
                                        "Please include the index of the task you wish to delete");
                            }
                            int deleteTaskId = Integer.parseInt(substrings[1]) - 1;
                            if (deleteTaskId >= 0 && deleteTaskId < tasks.size()) {
                                String details = tasks.get(deleteTaskId).toString();
                                tasks.remove(deleteTaskId);
                                String message = " Noted. I've removed this task:\n"
                                        + "     " + details
                                        + "\n Now you have " + tasks.size() + " tasks in the list.";
                                printLine(message);
                            } else { // user input is an integer bigger than size of task list
                                String message = tasks.size() > 0
                                        ? "No such task! Please enter a task ID between 1 and " + tasks.size()
                                        : "You have no tasks! Please add some tasks first";
                                throw new DukeException(message);
                            }
                            break;
                        case "todo":
                            if (substrings.length != 2) { // user input only has the command
                                throw new DukeException("Invalid command! Please include details of this task");
                            }
                            String todoDesc = substrings[1];
                            Todo todo = new Todo(todoDesc);
                            tasks.add(todo);
                            printLine(" Got it. I've added this task:\n"
                                    + "     " + todo
                                    + "\n Now you have " + tasks.size() + " tasks in the list.");
                            break;
                        case "deadline":
                            if (substrings.length != 2) { // user input only has the command eg "deadline"
                                throw new DukeException("Invalid command! Please include details of this task");
                            }
                            String[] details = substrings[1].split("/by", 2);
                            if (details.length != 2) { // user input does not have /by
                                throw new DukeException("Invalid command! Please include the deadline of this task");
                            }
                            Deadline deadline = new Deadline(details[0], details[1]);
                            tasks.add(deadline);
                            printLine(" Got it. I've added this task:\n"
                                    + "     " + deadline
                                    + "\n Now you have " + tasks.size() + " tasks in the list.");
                            break;
                        case "event":
                            if (substrings.length != 2) { // user input only has the command eg "event"
                                throw new DukeException("Invalid command! Please include details of this task");
                            }
                            String[] eventDetails = substrings[1].split("/from", 2);
                            if (eventDetails.length != 2) { // user input does not include /from
                                throw new DukeException("Invalid command! Please include when the event starts");
                            }
                            String[] eventTimings = eventDetails[1].split("/to", 2);
                            if (eventTimings.length != 2) { // user input does not include /to
                                throw new DukeException("Invalid command! Please include when the event ends");
                            }
                            Event event = new Event(eventDetails[0], eventTimings[0], eventTimings[1]);
                            tasks.add(event);
                            String message = " Got it. I've added this task:\n"
                                    + "     " + event
                                    + "\n Now you have " + tasks.size() + " tasks in the list.";
                            printLine(message);
                            break;
                        default:
                            throw new DukeException("Sorry! I do not recognise this command");
                    }
                } catch (NumberFormatException e) { // user inputs invalid argument for mark and unmark eg. "mark ab"
                    printLine("Invalid command! Please enter only one valid task ID");
                } catch (DukeException e) {
                    printLine(e.getMessage());
                }
            }
        }
        printLine(goodbye);
    }
}

