package agenda

import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

class OnTransaction implements TransactionSynchronization {

    private beforeCommitCallback
    private afterCommitCallback
    private afterRollbackCallback

    OnTransaction(beforeCommitCallback, afterCommitCallback=null, afterRollbackCallback=null) {
        this.beforeCommitCallback = beforeCommitCallback
        this.afterCommitCallback = afterCommitCallback
        this.afterRollbackCallback = afterRollbackCallback
        TransactionSynchronizationManager.registerSynchronization(this)
    }

    @Override
    void beforeCommit(boolean readOnly) {
        if (beforeCommitCallback) {
            beforeCommitCallback()
        }
    }

    @Override
    void beforeCompletion() {
    }

    @Override
    void afterCommit() {
        if (afterCommitCallback) {
            afterCommitCallback()
        }
    }

    @Override
    void afterCompletion(int status) {
        if (status != TransactionSynchronization.STATUS_COMMITTED && afterRollbackCallback) {
            afterRollbackCallback()
        }
    }

    @Override
    void flush() {
    }

    @Override
    void resume() {
    }

    @Override
    void suspend() {
    }
}
