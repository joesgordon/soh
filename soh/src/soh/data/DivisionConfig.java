package soh.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DivisionConfig
{
    /**  */
    public final Division div;
    /** The targetTime in tenths of a second. */
    public int targetTime;
    /** Target length in cm. */
    public int targetLength;

    /***************************************************************************
     * @param div
     **************************************************************************/
    public DivisionConfig( Division div )
    {
        this.div = div;
        this.targetTime = div.defaultTargetTime;
        this.targetLength = 130;
    }
}
