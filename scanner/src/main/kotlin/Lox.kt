import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

private val terminal = Terminal()
val hadError = ArrayList<Lox.ContextualError>()

class Lox {

    fun main(args: Array<String>) {
        if (args.size > 1) {
            return terminal.println("""
            ${brightRed("Usage invalid -> ")}
            ${brightRed("Usage: [script]")}
        """.trimIndent())
        }

        terminal.println(
            when (args.size) {
                1 -> {
                    runFile(args[0])
                    green("Interpreted file -> ")
                }
                else -> {
                    runPrompt()
                    green("Prompt execution stopped")
                }
            }
        )
    }

    fun runPrompt() {
        terminal.println(blue("<Crl-d> terminates prompting"))

        val input = InputStreamReader(System.`in`)
        val reader = BufferedReader(input)

        while (true) {
            resetErrors()
            terminal.print(green("?> "))
            val line = reader.readLine() ?: break
            run(line)
        }
    }

    private fun runFile(s: String) {
        val fileBytes = File(s).readBytes();
        run(String(fileBytes, Charset.defaultCharset()))
    }

    private fun run(block: String) {
        val scanner = Scanner(block);
        scanner.scanTokens().forEach {
            terminal.println(
                green(it.toString())
            )
        }
    }

    companion object {
        fun resetErrors() = hadError.clear()
        fun error(line: Int, message: String) {
            val newError = ContextualError(line, "", message)
            hadError.add(newError)
            terminal.danger(
                message = newError,
                stderr = true
            )
        }
    }


    data class ContextualError(val line: Int, val where: String, val message: String, val range: IntRange = IntRange(0,0)) {
        /*
        TODO: Make use of range to make this
            Error: Unexpected "," in argument list.
            15 | function(first, second,);
                                       ^-- Here.
       */

        private fun rangeText(): String = when {
            this.range.first == this.range.last && this.range.last == 0 -> ""
            else -> ":<${range.first}-${range.last}>"
        }

        override fun toString(): String {
            return "[line $line${rangeText()}] Error  $where  $message"
        }
    }

}
