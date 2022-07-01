package system

import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import component.InputComponent
import component.PlayerComponent

@AllOf([PlayerComponent::class])
class InputSystem(
    private val inputMapper: ComponentMapper<InputComponent>,
) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        inputMapper[entity].also { playerInput -> }
    }
}
