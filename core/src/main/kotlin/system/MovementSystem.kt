package system

import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import component.PlayerComponent

@AllOf([PlayerComponent::class])
class MovementSystem : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
    }
}
