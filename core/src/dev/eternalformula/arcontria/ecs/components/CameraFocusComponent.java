package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Simple component to tell the camera to focus on the entity
 * which this component is attached to. If more than one entity
 * has this component, the camera will only focus on the first entity
 * that was loaded with this component.
 * 
 * @author EternalFormula
 */

public class CameraFocusComponent implements Component {
}
