package sqlcmd.command;

import sqlcmd.view.View;

public class Help implements Command {

    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("'connect|database|user|password'\n" +
                "\t подключение к базе");
        view.write("'table|tableName|primaryKeyName|column1Name|column1Type|...|" +
                "columnNName|columnNType'\n" +
                "\t для создания таблицы");
        view.write("'list'\n" +
                "\t вывод списка всех таблиц");
        view.write("'find|tableName'\n" +
                "\t вывод всей таблицы");
        view.write("'find|tableName|limit|offset'\n" +
                "\t вывод части таблицы");
        view.write("'create|tableName|column1Value|...|columnNValue'\n" +
                "\t создание поля");
        view.write("'update|tableName|primaryKeyColumnName|primaryKeyValue|" +
                "column1Name|column1NewValue|...|" +
                "columnNName|columnNNewValue'\n" +
                "\t обновление поля");
        view.write("'delete|tableName|primaryKeyColumnName|primaryKeyValue'\n" +
                "\t удаление поле");
        view.write("'clear|tableName'\n" +
                "\t очистка таблицы");
        view.write("'drop|tableName'\n" +
                "\t удаление таблицы");
        view.write("'exit'\n" +
                "\t выход из програмы");
    }
}