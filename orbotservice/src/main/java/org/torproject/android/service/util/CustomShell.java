package org.torproject.android.service.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import com.jaredrummler.android.shell.ShellExitCode;
import com.jaredrummler.android.shell.StreamGobbler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomShell extends Shell {


    @WorkerThread
    public static CommandResult run(@NonNull String shell, boolean waitFor, @Nullable String[] env, @NonNull String command) {
        List<String> stdout = Collections.synchronizedList(new ArrayList<String>());
        List<String> stderr = Collections.synchronizedList(new ArrayList<String>());
        int exitCode = -1;

        try {

            // setup our process, retrieve stdin stream, and stdout/stderr gobblers
            //Process process = runWithEnv(command, env);
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("/system/bin/sh", "-c", command);
            Process process = builder.start();

         //   DataOutputStream stdin = new DataOutputStream(process.getOutputStream());
            StreamGobbler stdoutGobbler = null;
            StreamGobbler stderrGobbler = null;

            if (waitFor) {
                stdoutGobbler = new StreamGobbler(process.getInputStream(), stdout);
                stderrGobbler = new StreamGobbler(process.getErrorStream(), stderr);

                // start gobbling and write our commands to the shell
                stdoutGobbler.start();
                stderrGobbler.start();
            }

            /**

            try {
                for (String write : commands) {
                    stdin.write((write + " &\n").getBytes("UTF-8"));
                    stdin.flush();
                }

                if (waitFor)
                    stdin.write("exit\n".getBytes("UTF-8"));

                stdin.flush();
            } catch (IOException e) {
                //noinspection StatementWithEmptyBody
                if (e.getMessage().contains("EPIPE")) {
                    // method most horrid to catch broken pipe, in which case we do nothing. the command is not a shell, the
                    // shell closed stdin, the script already contained the exit command, etc. these cases we want the output
                    // instead of returning null
                } else {
                    // other issues we don't know how to handle, leads to returning null
                    throw e;
                }
            }**/

            // wait for our process to finish, while we gobble away in the background
            if (waitFor)
                exitCode = process.waitFor();
            else
                exitCode = 0;

            // make sure our threads are done gobbling, our streams are closed, and the process is destroyed - while the
            // latter two shouldn't be needed in theory, and may even produce warnings, in "normal" Java they are required
            // for guaranteed cleanup of resources, so lets be safe and do this on Android as well
           /**
            try {
                stdin.close();
            } catch (IOException e) {
                // might be closed already
            }**/

           if (waitFor) {
               stdoutGobbler.join();
               /* ********OpenRefactory Warning********
			 Possible null pointer Dereference!
			 Path: 
				File: CustomShell.java, Line: 37
					StreamGobbler stderrGobbler=null;
					Variable stderrGobbler is initialized null.
				File: CustomShell.java, Line: 41
					stderrGobbler=new StreamGobbler(process.getErrorStream(),stderr);
					Variable stderrGobbler is allocated.
				File: CustomShell.java, Line: 90
					stderrGobbler.join();
					stderrGobbler is referenced in method invocation.
			*/
			stderrGobbler.join();
           }

        } catch (InterruptedException e) {
            exitCode = ShellExitCode.WATCHDOG_EXIT;
        } catch (IOException e) {
            exitCode = ShellExitCode.SHELL_WRONG_UID;
        }

        return new CommandResult(stdout, stderr, exitCode);
    }
}
