# simple-crm-rest-and-spa

This is a REST + SPA application.
Server side (REST API) written in J2EE (Servlets with JSON responses using GSON lib).
Hibernate for persistence in SQL (SQL DB structure dump in MySQL folder).
Simple HttpSession based authentication.
JUnit tests added to several methods using mocking with EasyMock library.
Ant build file provided (Server originally deployed using Tomcat).
Client side (web front-end) using AJAX requests and custom MVC architecture: promises with Q.js, routing with Sammy.js, modules loading with Require.js, templates with Handlebars.js, jQuery Ajax and DOM manipulations.