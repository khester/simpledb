package dbms.query;

import dbms.Consts;
import dbms.command.CommandResult;
import dbms.schema.Row;
import dbms.schema.Schema;
import dbms.storage.BufferManager;

import java.io.IOException;

public class QueryManager {

    public CommandResult executeCommand(String query) throws IOException {
        QueryPlan queryPlan = new QueryPlan(query);
        QueryResult queryResult = BufferManager.getInstance().executeQuery(queryPlan);
        CommandResult commandResult = new CommandResult();
        commandResult.setStatus(Consts.STATUS_COMMAND_OK);

        queryResult.getResults().forEach(row -> System.out.println(row.getData()));

        System.out.println();

        return commandResult;
    }


}
