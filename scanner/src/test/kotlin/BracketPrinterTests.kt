import grammar.*
import grammar.Grouping
import grammar.representation.BracketPrinter
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BracketPrinterTests {

    @Test
    fun `Given an expression, group the expression with brackets`() {
        val expression: Expr =
            Binary(
                left = Unary(
                    operator = Token(
                        type = TokenType.MINUS,
                        lexeme = "-",
                    ),
                    expression = Literal(
                        value = 123
                    )
                ),
                operator = Token(
                    type = TokenType.STAR,
                    lexeme = "*",
                ),
                right = Grouping(
                    expression = Literal(
                        value = 45.67
                    )
                )
            )
        assertEquals(
            expected = "(* (- 123) (group 45.67))",
            actual = BracketPrinter().print(expression)
        )
    }
}
