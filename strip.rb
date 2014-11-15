require 'sqlite3'
require 'sequel'

DB = Sequel.sqlite DB_FILE

alter_table(:items) do
    add_column :stripped_text, String, :text => true
end