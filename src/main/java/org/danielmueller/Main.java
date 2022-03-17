package org.danielmueller;

import java.util.Objects;

/**
 * Main class.
 * 
 * @author KNIME GmbH
 */
public class Main
{
	private static MainApi api;

	/**
	 * main method serving as quasi API endpoint, solely passing request along to
	 * Controller that deals with it.
	 *
	 * @param args command line arguments to be processed as such as bash-run programs
	 * @throws Exception needs to be generic due to different scenarios in which
	 * different exception may be thrown
	 */
	public static void main(String[] args) throws Exception
	{
		getMainApiInstance().commandLineProcessFile(args);
	}

	/**
	 *
	 * @return {@link MainApi} implementation
	 */
	private static MainApi getMainApiInstance()
	{
		if (Objects.isNull(api))
		{
			api = new MainApiController();
		}

		return api;
	}
}
