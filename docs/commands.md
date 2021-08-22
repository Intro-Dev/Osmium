## Option commands

### set
This command sets an option to be overridden by a server. 
The 3 variations of the command are

- Double {identifier} {double}
- Boolean {identifier} {boolean}
- Enum {identifier} {enum_type} {enum_value}

A list of option identifiers and types can be found in docs/options.md

### reset

This command deletes all previously set options

### refresh

This command refreshes any changes made to options

Note: After any changes the refresh command must be called to update currently connected players

### remove

This command removes a command from the database of set commands