import grammar.TokenType.*

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ScannerTest {

    @AfterTest
    fun after() = Lox.resetErrors()

    @Test
    fun `Given a literal number and dot without numeric meaning tokenize number and dot individually`() = listOf(
        "123.",
        "123..111",
        ".123",
    ).forEach { it ->
        val tokens = Scanner(it).scanTokens()
        tokens.map { it.type }.contains(DOT)
        tokens.map { it.type }.contains(NUMBER)
    }

    @Test
    fun `Given a literal number tokenize should store the number in the token`() = listOf(
        "098",
        "123.123",
    ).forEach {
        Lox.resetErrors()
        val tokens = Scanner(it).scanTokens()
        tokens.first().run {
            assertEquals(NUMBER, this.type)
            assertEquals(it.toDouble(), this.literal as Double)
        }

        assertEquals(2, tokens.size)
        assertEquals(0, hadError.size)
        assertEquals(EOF, tokens.last().type)
    }

    @Test
    fun `Given a literal string tokenize should store the string in the token`() = listOf(
        "\"String\"",
        "\";\"",
        "\";/\"",
    ).forEach {
        val tokens = Scanner(it).scanTokens()
        tokens.first().run {
            assertEquals(STRING, this.type)
            assertEquals(it, this.literal.toString())
        }

        assertEquals(2, tokens.size)
        assertEquals(EOF, tokens.last().type)
    }

    @Test
    fun `Given na invalid value tokenize should report an error`() = listOf(
        "#",
        "\"",
    ) .forEach {
        val tokens = Scanner(it).scanTokens()
        assertEquals(1, tokens.size)
        assertEquals(EOF, tokens.last().type)
        assertEquals(1, hadError.size)
        Lox.resetErrors()
    }

    @Test
    fun `Given a comment and white space tokenize should skip`() = listOf(
        "///////",
        " ",
        "\n",
        "\t",
    ).forEach {
        Lox.resetErrors()
        val tokens = Scanner(it).scanTokens()

        assertEquals(1, tokens.size)
        assertEquals(0, hadError.size)
        assertEquals(EOF, tokens.last().type)
    }

    @Test
    fun `Given a string tokenize all valid values`() = listOf(
        // SINGLE
        LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE,
        RIGHT_BRACE, COMMA, DOT, MINUS,
        PLUS, SEMICOLON, STAR, BANG,
        EQUAL, LESS, GREATER, SLASH,
        // SINGLE OR DOUBLE
        GREATER_EQUAL, LESS_EQUAL, BANG_EQUAL, EQUAL_EQUAL,
        // Literals
        /*
            IDENTIFIER, STRING, NUMBER,
            // KEYWORDS
            AND, CLASS, ELSE, FALSE, FUN,
            FOR, IF, NIL, OR, PRINT, RETURN,
            SUPER, THIS, TRUE, VAR, WHILE, EOF
             */
    ).forEach {
        it.classification.run {
            Lox.resetErrors()
            val tokens = Scanner(this.first).scanTokens()
            assertEquals(2, tokens.size)
            assertEquals(
                it.toString(),
                tokens.first().type.toString()
            )
            assertEquals(0, hadError.size)
            assertEquals(EOF, tokens.last().type)
        }
    }
}
