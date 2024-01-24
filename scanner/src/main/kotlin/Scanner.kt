import grammar.*

class Scanner(private val source: String) {
    private val keyWords = hashMapOf(
        "and"    to    TokenType.AND,
        "class"  to  TokenType.CLASS,
        "else"   to   TokenType.ELSE,
        "false"  to  TokenType.FALSE,
        "fun"    to    TokenType.FUN,
        "for"    to    TokenType.FOR,
        "if"     to     TokenType.IF,
        "nil"    to    TokenType.NIL,
        "or"     to     TokenType.OR,
        "print"  to  TokenType.PRINT,
        "return" to TokenType.RETURN,
        "super"  to  TokenType.SUPER,
        "this"   to   TokenType.THIS,
        "true"   to   TokenType.TRUE,
        "var"    to    TokenType.VAR,
        "while"  to  TokenType.WHILE,
    )

    private val tokens = ArrayList<Token>()
    private var start = 0
    private var current = 0
    private var line = 1

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            this.start = current
            scanToken()
        }
        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        val tokenType = when (this.advance()) {
            '(' -> TokenType.LEFT_PAREN
            ')' -> TokenType.RIGHT_PAREN
            '{' -> TokenType.LEFT_BRACE
            '}' -> TokenType.RIGHT_BRACE
            ',' -> TokenType.COMMA
            '.' -> TokenType.DOT
            '-' -> TokenType.MINUS
            '+' -> TokenType.PLUS
            ';' -> TokenType.SEMICOLON
            '*' -> TokenType.STAR
            '!' -> if (testConsumeMatch('=')) TokenType.BANG_EQUAL else  TokenType.BANG
            '=' -> if (testConsumeMatch('=')) TokenType.EQUAL_EQUAL else  TokenType.EQUAL
            '<' -> if (testConsumeMatch('=')) TokenType.LESS_EQUAL else  TokenType.LESS
            '>' -> if (testConsumeMatch('=')) TokenType.GREATER_EQUAL else  TokenType.GREATER
            // Consume comment lines
            '/' -> if (testConsumeMatch('/')) consumeLine() else TokenType.SLASH
            '"' -> evaluateLiteralString()
            in numericRange -> evaluateLiteralNumber()
            in alphaRange -> evaluateIdentifier()
            // White space
            ' ', '\r', '\t' -> null
            '\n' ->  { line++; null }
            else -> {
                Lox.error(line,  "Unexpected Character.")
                null
            }
        } ?: return

        when (tokenType) {
            is TokenType -> tokens.add(
                Token(
                    type = tokenType,
                    lexeme = source.substring(IntRange(start, current - 1)),
                    literal = null,
                    line = line
                )
            )
            is Pair<*, *> -> {
                val (type, literal) = tokenType
                when (type) {
                    is TokenType -> {
                        tokens.add(
                            Token(
                                type = type,
                                lexeme = source.substring(IntRange(start, current-1)),
                                literal = literal,
                                line = line
                            )
                        )
                    }
                }
            }
        }
    }

    private val numericRange = '0'..'9'
    private val alphaRange = ('a'..'z') + ('A'..'Z') + '_'
    private val alphaNumeric = numericRange + alphaRange

    private fun isAtEnd(offset: Int = 0): Boolean = (current + offset) >= source.length

    private fun peek(): Char? = if (isAtEnd()) null else source[current]
    private fun peekNext(): Char? = if (isAtEnd(1)) null else source[current + 1]
    private fun advance(): Char = source[current++]
    private fun consumeLine(): TokenType? {
        while (peek() != '\n' && !isAtEnd()) { advance() }
        return null
    }

    private fun testConsumeMatch(expected: Char): Boolean = when {
        isAtEnd() -> false
        source[current] != expected -> false
        else -> { current ++; true }
    }

    private fun evaluateLiteralString(): Pair<TokenType?, String?> {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        return if (isAtEnd()) {
            Lox.error(line, "Unterminated string.")
            (null to null)
        } else {
            advance()
            (TokenType.STRING to source.substring(IntRange(start, current - 1)))
        }
    }

    private fun evaluateLiteralNumber(): Pair<TokenType?, Double?>  {
        while (when (peek()) {
                in numericRange -> true
                '.' -> when (peekNext()) {
                    in numericRange -> true
                    else -> false
                }
                else -> false
            }) {
            advance()
        }
        val numericLiteral = source.substring(IntRange(start, current -1)).toDoubleOrNull()
        return if (numericLiteral == null) {
            Lox.error(line, "Number can not be determined.")
            (null to null)
        } else {
            (TokenType.NUMBER to numericLiteral)
        }
    }

    private fun evaluateIdentifier(): TokenType {
        while (when (peek()) {in alphaNumeric -> true; else -> false }) {
            advance()
        }
        return keyWords.getOrElse(source.substring(start, current - 1)) {
            TokenType.IDENTIFIER
        }
    }
}
