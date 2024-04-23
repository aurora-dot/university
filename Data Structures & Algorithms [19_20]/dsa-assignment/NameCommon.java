
/**
 * The common traits between Vertex and Edge to inherit
 *
 * @author (your name)
 * @version 30/04/2020
 */

public class NameCommon {
    private String name;

    /**
     * The constructor - sets the name of the object
     *
     * @param  e  The edge to remove
     * @return    The old name of the edge
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the name of the object
     *
     * @return    The name of the object
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name of the object
     *
     * @param  name  The edges new name to set
     */
    public NameCommon(String name) {
        setName(name);
    }
}
