package me.n0rdy.hackattic.util;

public final class HackatticRemoteUrlBuilder {
    private static final String HACKATTIC_CHALLENGES_URL = "https://hackattic.com/challenges";
    private static final String ACCESS_TOKEN_ENV_VAR = "access_token";
    private static final String PROBLEM_PATH = "problem";
    private static final String SOLUTION_PATH = "solve";

    private HackatticRemoteUrlBuilder() {}

    public static String buildProblemUri(String taskName) {
        return buildUri(taskName, PROBLEM_PATH);
    }

    public static String buildSolutionUri(String taskName) {
        return buildUri(taskName, SOLUTION_PATH);
    }

    private static String buildUri(String taskName, String path) {
        String accessToken = System.getenv(ACCESS_TOKEN_ENV_VAR);
        return String.format("%s/%s/%s?access_token=%s", HACKATTIC_CHALLENGES_URL, taskName, path, accessToken);
    }
}
