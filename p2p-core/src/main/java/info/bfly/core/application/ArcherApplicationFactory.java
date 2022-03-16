package info.bfly.core.application;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

import org.omnifaces.application.OmniApplication;

/**
 *
 *
 * Description:
 */
public class ArcherApplicationFactory extends ApplicationFactory {
    // Variables
    // ------------------------------------------------------------------------------------------------------
    private final    ApplicationFactory wrapped;
    private volatile Application        application;

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Construct a new OmniFaces application factory around the given wrapped
     * factory.
     *
     * @param wrapped The wrapped factory.
     */
    public ArcherApplicationFactory(ApplicationFactory wrapped) {
        this.wrapped = wrapped;
    }

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Returns a new instance of {@link OmniApplication} which wraps the
     * original application.
     */
    @Override
    public Application getApplication() {
        if (application == null) {
            application = new ArcherApplication(wrapped.getApplication());
        }
        return application;
    }

    /**
     * Returns the wrapped factory.
     */
    @Override
    public ApplicationFactory getWrapped() {
        return wrapped;
    }

    /**
     * Sets the given application instance as the current instance. If it's not
     * an instance of {@link OmniApplication}, then it will be wrapped by
     * {@link OmniApplication}.
     */
    @Override
    public synchronized void setApplication(Application application) {
        this.application = (application instanceof ArcherApplication) ? application : new ArcherApplication(application);
        wrapped.setApplication(this.application);
    }
}
