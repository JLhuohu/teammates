package teammates.client.scripts.sql;

import com.googlecode.objectify.cmd.Query;

import teammates.storage.sqlentity.AccountRequest;

/**
 * Data migration class for account request entity.
 */
public class DataMigrationForAccountRequestSql
        extends DataMigrationEntitiesBaseScriptSql<teammates.storage.entity.AccountRequest, AccountRequest> {

    public static void main(String[] args) {
        new DataMigrationForAccountRequestSql().doOperationRemotely();
    }

    @Override
    protected Query<teammates.storage.entity.AccountRequest> getFilterQuery() {
        // returns all AccountRequest entities
        return ofy().load().type(teammates.storage.entity.AccountRequest.class);
    }

    /**
     * Set to true to preview the migration without actually performing it.
     */
    @Override
    protected boolean isPreview() {
        return false;
    }

    /**
     * Always returns true, as the migration is needed for all entities from
     * Datastore to CloudSQL.
     */
    @Override
    protected boolean isMigrationNeeded(teammates.storage.entity.AccountRequest accountRequest) {
        return true;
    }

    @Override
    protected void migrateEntity(teammates.storage.entity.AccountRequest oldEntity) throws Exception {
        AccountRequest newEntity = new AccountRequest(
                oldEntity.getEmail(),
                oldEntity.getName(),
                oldEntity.getInstitute());

        // set registration key to the old value if exists
        if (oldEntity.getRegistrationKey() != null) {
            newEntity.setRegistrationKey(oldEntity.getRegistrationKey());
        }

        // set registeredAt to the old value if exists
        if (oldEntity.getRegisteredAt() != null) {
            newEntity.setRegisteredAt(oldEntity.getRegisteredAt());
        }

        // for the createdAt, the Hibernate annotation will auto generate the value
        // always even if we set it.

        // for the updatedAt, we will let the db auto generate since this is the latest
        // update time is during the migration

        // for the id, we need to use the new UUID, since the old id is email +
        // institute with % as delimiter

        saveEntityDeferred(newEntity);
    }
}
