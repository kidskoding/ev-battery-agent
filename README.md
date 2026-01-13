# ev-battery-agent
an autonomous AI agent built using **Java** and **LangChain4j** that proactively monitors battery health in various iconic EVs like Rivian and Tesla

If a problem occurs (such as a battery thermal risk or a software defect) the agent sends a high priority ticketing request via **Atlassian Jira** so engineers can fix the issue before it leads to a recall.

1. **Monitoring**: Using **LangChain4j** (Java SDK for LangChain) and **Java**, agent will monitor/watch the EV batteries' live data (temperature and voltage)
2. **Thinking**: The agent opens up a thought process, using RAG to read manuals from big EV car companies like Rivian/Tesla and decide if the data is dangerous
3. **Acting**: AI Agent logs into **Atlassian Jira** and creates a request/ticket for human engineers to fix
