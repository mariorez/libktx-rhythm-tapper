data class Action(
    val name: Name,
    val type: Type
) {
    enum class Name { F, G, H, J }
    enum class Type { START, END }
}
