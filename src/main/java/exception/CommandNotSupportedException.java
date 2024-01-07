package exception;

import localization.Messages;

/**
 * @author Ihor Vasyliev
 * @since 2024/01/07
 */
public class CommandNotSupportedException extends RuntimeException {

	public enum Command {
		PRINT(Messages.Log.PRINT_COMMAND_NOT_REGISTRATED);

		private final String errorMessage;
		Command(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}

	public CommandNotSupportedException(Command command) {
		super(command.getErrorMessage());
	}
}
