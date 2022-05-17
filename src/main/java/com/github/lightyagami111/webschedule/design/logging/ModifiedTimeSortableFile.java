/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lightyagami111.webschedule.design.logging;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

/**
 *
 * @author ivaylo
 */
class ModifiedTimeSortableFile extends File implements Serializable, Comparable<File>
{
	private static final long serialVersionUID = 1373373728209668895L;
	
	public ModifiedTimeSortableFile(String parent, String child) {
		super(parent, child);
	}

	public ModifiedTimeSortableFile(URI uri) {
		super(uri);
	}

	public ModifiedTimeSortableFile(File parent, String child) {
		super(parent, child);
	}	
	
	public ModifiedTimeSortableFile(String string) {
		super(string);
	}
	
        @Override
	public int compareTo(File anotherPathName) {
		long thisVal = this.lastModified();
		long anotherVal = anotherPathName.lastModified();
		return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
	}
}