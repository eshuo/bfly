package info.bfly.archer.common.exception;

/**
 * Description:找不到对象 exception，即通过id或者其他条件，未查找到任何对象。
 *
 */
public class NoMatchingObjectsException extends Exception {
    private static final long serialVersionUID = -574118690577007425L;
    private Class clazz;

    public NoMatchingObjectsException(Class clazz, String msg) {
        super(msg);
        this.clazz = clazz;
    }

    public NoMatchingObjectsException(Class clazz, String msg, Throwable e) {
        super(msg, e);
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
