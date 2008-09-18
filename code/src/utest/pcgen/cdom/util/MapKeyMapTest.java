/*
 * MapKeyMapTest.java
 * Copyright 2008 (C) James Dempsey
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on 14/09/2008 09:53:37
 *
 * $Id: $
 */
package pcgen.cdom.util;

import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;

import pcgen.cdom.enumeration.AspectName;
import pcgen.cdom.enumeration.MapKey;
import pcgen.cdom.helper.Aspect;

/**
 * The Class <code>MapKeyMapTest</code> test that the MapKeyMap
 * class is functioning correctly. 
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class MapKeyMapTest extends TestCase
{
	
	private static final String BREED = "shetland sheepdog";
	private static final String AGE = "1";
	private static final String NAME = "Mistletoe";

	/** The map key map. */
	private MapKeyMap mapKeyMap;
	
	private AspectName ageKey;
	private AspectName nameKey;
	private AspectName breedKey;

	private Aspect ageAspect;
	private Aspect nameAspect;
	private Aspect breedAspect;

	/* (non-Javadoc)
	 * @see pcgen.PCGenTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		mapKeyMap = new MapKeyMap();
		ageKey = AspectName.getConstant("agE");
		nameKey = AspectName.getConstant("Name");
		breedKey = AspectName.getConstant("breed");
		
		ageAspect = new Aspect("age", AGE);
		mapKeyMap.addToMapFor(MapKey.ASPECT, ageKey, ageAspect);
		nameAspect = new Aspect("name", NAME);
		mapKeyMap.addToMapFor(MapKey.ASPECT, nameKey, nameAspect);
		breedAspect = new Aspect("breed", BREED);
		mapKeyMap.addToMapFor(MapKey.ASPECT, breedKey, breedAspect);
		
		super.setUp();
	}

	/**
	 * Test retrieval of items from the map
	 */
	@Test
	public void testGet()
	{
		assertEquals("Retrieve 3rd item by both keys", breedAspect, mapKeyMap
			.get(MapKey.ASPECT, breedKey));
		assertEquals("Retrieve 2nd item by both keys", nameAspect, mapKeyMap
			.get(MapKey.ASPECT, nameKey));
		assertEquals("Retrieve 1st item by both keys", ageAspect, mapKeyMap
			.get(MapKey.ASPECT, ageKey));
	}

	/**
	 * Test loading the map with another map.
	 */
	@Test
	public void testPutAll()
	{
		MapKeyMap newMap = new MapKeyMap();

		assertEquals("Expect an empty map intially", null, newMap
			.get(MapKey.ASPECT, ageKey));
		
		newMap.putAll(mapKeyMap);
		assertEquals("Retrieve 3rd item by both keys", breedAspect, newMap
			.get(MapKey.ASPECT, breedKey));
		assertEquals("Retrieve 1st item by both keys", ageAspect, newMap
			.get(MapKey.ASPECT, ageKey));
	}

	/**
	 * Test adding an entry to the map overwriting an existing entry.
	 */
	@Test
	public void testAddToMapFor()
	{
		assertEquals("Validate initial value of age", ageAspect, mapKeyMap
			.get(MapKey.ASPECT, ageKey));
		Aspect newage = new Aspect("age", "2");
		mapKeyMap.addToMapFor(MapKey.ASPECT, ageKey, newage);
		assertEquals("Validate initial value of age", newage, mapKeyMap
			.get(MapKey.ASPECT, ageKey));
	}

	/**
	 * Test removing items from the list.
	 */
	@Test
	public void testRemoveFromListFor()
	{
		assertEquals("Validate initial value of breed", breedAspect, mapKeyMap
			.get(MapKey.ASPECT, breedKey));
		assertTrue("Should be true as item is present", mapKeyMap
			.removeFromMapFor(MapKey.ASPECT, breedKey));
		assertEquals("Validate breed is no longer present", null, mapKeyMap
			.get(MapKey.ASPECT, breedKey));
		assertFalse("Should be false as item is no longer present", mapKeyMap
			.removeFromMapFor(MapKey.ASPECT, breedKey));
	}

	/**
	 * Test retrieval of the primary key set and that the retrieved set is 
	 * not a pointer to the master set in the MapKeyMap.
	 */
	@Test
	public void testGetKeySet()
	{
		Set<MapKey<?, ?>> keySet = mapKeyMap.getKeySet();

		assertEquals("only one primary key", 1, keySet.size());
		assertEquals("Only element should be an aspect", MapKey.ASPECT, keySet
			.toArray()[0]);
		
		mapKeyMap.addToMapFor(MapKey.TEST, "foo", "bar");
		assertEquals(
			"Still only one primary key, returned set should be independant of main collection",
			1, keySet.size());
		assertFalse("Set should not include test yet", keySet.contains(MapKey.TEST));

		keySet = mapKeyMap.getKeySet();
		assertEquals("Now two primary keys", 2, keySet.size());
		assertTrue("Set should include aspect", keySet.contains(MapKey.ASPECT));
		assertTrue("Set should include test", keySet.contains(MapKey.TEST));
	}

	/**
	 * Test the containsMapFor method and the effect of adding and 
	 * removing items on it. 
	 */
	@Test
	public void testContainsMapFor()
	{
		assertTrue("Should have ASPECT", mapKeyMap.containsMapFor(MapKey.ASPECT));
		assertFalse("Should not have TEST", mapKeyMap.containsMapFor(MapKey.TEST));

		mapKeyMap.addToMapFor(MapKey.TEST, "foo", "bar");
		assertTrue("Should have ASPECT", mapKeyMap.containsMapFor(MapKey.ASPECT));
		assertTrue("Should have TEST now", mapKeyMap.containsMapFor(MapKey.TEST));

		assertTrue("Should be true as item is present", mapKeyMap
			.removeFromMapFor(MapKey.TEST, "foo"));
		assertTrue("Should have ASPECT", mapKeyMap.containsMapFor(MapKey.ASPECT));
		assertFalse("Should not have TEST", mapKeyMap.containsMapFor(MapKey.TEST));

		assertTrue("Should be true as item is present", mapKeyMap
			.removeFromMapFor(MapKey.ASPECT, breedKey));
		assertTrue("Should still have ASPECT", mapKeyMap.containsMapFor(MapKey.ASPECT));
		assertFalse("Should not have TEST", mapKeyMap.containsMapFor(MapKey.TEST));
	}
	
	
	/**
	 * Test the removal of a map by key.
	 */
	@Test
	public void testRemoveMapFor()
	{
		assertTrue("Should have ASPECT", mapKeyMap
			.containsMapFor(MapKey.ASPECT));

		Map<AspectName, Aspect> removed = mapKeyMap.removeMapFor(MapKey.ASPECT);
		assertFalse("Should not have ASPECT", mapKeyMap
			.containsMapFor(MapKey.ASPECT));
		assertEquals("Removed map should have all expected elements", 3,
			removed.size());
		assertEquals("Retrieve 3rd item", breedAspect, removed
			.get(breedKey));
		assertEquals("Retrieve 2nd item", nameAspect, removed
			.get(nameKey));
		assertEquals("Retrieve 1st item", ageAspect, removed
			.get(ageKey));
	}
}
