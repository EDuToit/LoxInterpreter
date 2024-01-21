class Token(
    val type: TokenType,
    private val lexeme: String,
    val literal: Any?,
    line: Int
) {
    override fun toString(): String {
        return "$type $lexeme ${literal?.toString() ?: ""}"
    }
}

enum class TokenType(val classification: Pair<String, Classification>) {
    //SINGLE_CHAR
    LEFT_PAREN      ("(" to Classification.SINGLE_CHAR),
    RIGHT_PAREN     (")" to Classification.SINGLE_CHAR),
    LEFT_BRACE      ("{" to Classification.SINGLE_CHAR),
    RIGHT_BRACE     ("}" to Classification.SINGLE_CHAR),
    COMMA           ("," to Classification.SINGLE_CHAR),
    DOT             ("." to Classification.SINGLE_CHAR),
    MINUS           ("-" to Classification.SINGLE_CHAR),
    PLUS            ("+" to Classification.SINGLE_CHAR),
    SEMICOLON       (";" to Classification.SINGLE_CHAR),
    SLASH           ("/" to Classification.SINGLE_CHAR),
    STAR            ("*" to Classification.SINGLE_CHAR),

    // ONE_OR_TWO
    BANG            ("!" to Classification.ONE_OR_TWO),
    BANG_EQUAL      ("!=" to Classification.ONE_OR_TWO),
    EQUAL           ("=" to Classification.ONE_OR_TWO),
    EQUAL_EQUAL     ("==" to Classification.ONE_OR_TWO),
    GREATER         (">" to Classification.ONE_OR_TWO),
    GREATER_EQUAL   (">=" to Classification.ONE_OR_TWO),
    LESS            ("<" to Classification.ONE_OR_TWO),
    LESS_EQUAL      ("<=" to Classification.ONE_OR_TWO),

    // LITERALS
    IDENTIFIER      ("'id" to Classification.LITERALS),
    STRING          ("\"\"" to Classification.LITERALS),
    NUMBER          ("\\d" to Classification.LITERALS),

    // KEYWORDS
    AND             ("and" to Classification.KEYWORDS),
    CLASS           ("class" to Classification.KEYWORDS),
    ELSE            ("else" to Classification.KEYWORDS),
    FALSE           ("false" to Classification.KEYWORDS),
    FUN             ("fun" to Classification.KEYWORDS),
    FOR             ("for" to Classification.KEYWORDS),
    IF              ("if" to Classification.KEYWORDS),
    NIL             ("nil" to Classification.KEYWORDS),
    OR              ("or" to Classification.KEYWORDS),
    PRINT           ("print" to Classification.KEYWORDS),
    RETURN          ("return" to Classification.KEYWORDS),
    SUPER           ("super" to Classification.KEYWORDS),
    THIS            ("this" to Classification.KEYWORDS),
    TRUE            ("true" to Classification.KEYWORDS),
    VAR             ("var" to Classification.KEYWORDS),
    WHILE           ("while" to Classification.KEYWORDS),
    EOF             ("\\0" to Classification.KEYWORDS);

    enum class Classification {
        SINGLE_CHAR,
        ONE_OR_TWO,
        LITERALS,
        KEYWORDS
    }
}