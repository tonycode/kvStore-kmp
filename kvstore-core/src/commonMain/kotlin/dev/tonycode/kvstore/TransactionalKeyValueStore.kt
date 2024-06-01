package dev.tonycode.kvstore


interface TransactionalKeyValueStore {

    fun onCommand(command: Command): ExecutionResult


    sealed class Command {
        /**
         * Store the value for key
         */
        data class Set(val key: String, val value: String) : Command()

        /**
         * Return the current value for key
         */
        data class Get(val key: String) : Command()

        /**
         * Remove the entry for key
         */
        data class Delete(val key: String) : Command()

        /**
         * Return the number of keys that have the given value
         */
        data class Count(val value: String) : Command()

        /**
         * Start a new transaction
         */
        data object Begin : Command()

        /**
         * Complete the current transaction
         */
        data object Commit : Command()

        /**
         * Revert to state prior to BEGIN call
         */
        data object Rollback : Command()

        companion object {
            fun fromString(command: String): Command {
                val tokens = command.split(Regex("\\s+"))
                if (tokens.isEmpty()) throw IllegalArgumentException(MSG_ERROR_EMPTY_COMMAND)

                return when (tokens[0].uppercase()) {
                    "SET" -> if (tokens.size == 3) {
                        Set(tokens[1], tokens[2])
                    } else throw IllegalArgumentException(MSG_ERROR_WRONG_COMMAND_ARGUMENTS)

                    "GET" -> if (tokens.size == 2) {
                        Get(tokens[1])
                    } else throw IllegalArgumentException(MSG_ERROR_WRONG_COMMAND_ARGUMENTS)

                    "DELETE" -> if (tokens.size == 2) {
                        Delete(tokens[1])
                    } else throw IllegalArgumentException(MSG_ERROR_WRONG_COMMAND_ARGUMENTS)

                    "COUNT" -> if (tokens.size == 2) {
                        Count(tokens[1])
                    } else throw IllegalArgumentException(MSG_ERROR_WRONG_COMMAND_ARGUMENTS)

                    "BEGIN" -> if (tokens.size == 1) {
                        Begin
                    } else throw IllegalArgumentException(MSG_ERROR_WRONG_COMMAND_ARGUMENTS)

                    "COMMIT" -> if (tokens.size == 1) {
                        Commit
                    } else throw IllegalArgumentException(MSG_ERROR_WRONG_COMMAND_ARGUMENTS)

                    "ROLLBACK" -> if (tokens.size == 1) {
                        Rollback
                    } else throw IllegalArgumentException(MSG_ERROR_WRONG_COMMAND_ARGUMENTS)

                    else -> throw IllegalArgumentException(MSG_ERROR_UNKNOWN_COMMAND)
                }
            }
        }
    }

    sealed class ExecutionResult {
        data object Success : ExecutionResult()

        data class SuccessWithResult(val result: String) : ExecutionResult()

        data class SuccessWithIntResult(val result: Int) : ExecutionResult()

        data class Error(val errorMessage: String) : ExecutionResult()
    }

    companion object {
        val commands = listOf(  // Pair(commandKey, commandArgs)
            Pair("SET", "key value"),
            Pair("GET", "key"),
            Pair("DELETE", "key"),
            Pair("COUNT", "value"),
            Pair("BEGIN", null),
            Pair("COMMIT", null),
            Pair("ROLLBACK", null),
        )
    }

}
