package ro.octa.greendaosample.test;

import java.io.IOException;

import ro.octa.greendaosample.test.generator.Generator;

/**
 * @author aivarsda
 *
 */
public class Main 
{
	public Main() throws IOException {	}

	public static void main(String[] args) 
	{
		new Generator().generate();
	}
}
