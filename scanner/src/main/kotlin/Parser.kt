import grammar.Expr
import grammar.ParseError
import grammar.Token
import grammar.TokenType
import grammar.TokenType.*

@Suppress("unused", "SameParameterValue")
class Parser(private val tokens: List<Token>) {
    var current = 0

    fun parse(): Expr? {
        return try {
            expression()
        } catch (e: ParseError) {
            null
        }
    }

    private fun synchronize() {
        advance()
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) {
                return
            }
            when (peek().type) {
                CLASS, FUN, FOR, IF, PRINT, RETURN, VAR, WHILE -> return
                else -> advance()
            }
        }
    }

    fun expression() : Expr = equality()
    private fun equality(): Expr {
        var expr = comparison()
        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun comparison(): Expr {
        var expr = term()
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            val operator = previous()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun term(): Expr {
        var expr = factor()
        while (match(MINUS, PLUS)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun factor(): Expr {
        var expr = unary()
        while (match(SLASH, STAR)) {
            val operator = previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun unary(): Expr {
        if (match(BANG, MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }
        return primary()
    }

    private fun primary(): Expr {
        return when {
            match(FALSE) -> Expr.Literal(false)
            match(TRUE) -> Expr.Literal(true)
            match(NIL) -> Expr.Literal(null)
            match(NUMBER, STRING) -> Expr.Literal(previous().literal)
            match(LEFT_PAREN) -> {
                val expr = expression()
                consume(RIGHT_PAREN, "Expect ')' after expression.")
                return Expr.Grouping(expr)
            }
            else -> throw parseError(peek(), "Expect expression.")
        }
    }

    private fun consume(type: TokenType, message: String) : Token {
        if (check(type)) {
            return advance()
        } else {
            throw parseError(peek(), message)
        }
    }

    private fun parseError(token: Token, message: String): ParseError {
        Lox.error(token, message)
        return ParseError()
    }

    private fun match(vararg tokens: TokenType): Boolean {
        tokens.forEach {
            if (check(it)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType) : Boolean = when {
        isAtEnd() -> false
        else -> peek().type == type
    }

    private fun advance() : Token {
        return when {
            isAtEnd() -> tokens[current++]
            else -> previous()
        }
    }
    private fun peek(): Token = tokens[current]
    private fun isAtEnd(): Boolean = peek().type == EOF
    private fun previous(): Token = tokens[current - 1]
}