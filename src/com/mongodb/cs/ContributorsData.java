package com.mongodb.cs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContributorsData {

		
	public static List<Contributor> generateData() {
	
		List<Contributor> mUsersList = new ArrayList<Contributor>();
		Contributor mUser;
		
		// 1 record
		for(int i = 0; i < 2; i++) {
			mUser = new Contributor();
			mUser.setuserId("id"+i);
			mUser.setRepoName("charts");
			mUser.setDate(new Date());
			mUser.setRevision(i);
			mUsersList.add(mUser);
		}
		// 2 record
		for(int i = 0; i < 3; i++) {
			mUser = new Contributor();
			mUser.setuserId("id"+i);
			mUser.setRepoName("CorporateSource");
			mUser.setDate(new Date());
			mUser.setRevision(i);
			mUsersList.add(mUser);
		}
		// 1 record
		for(int i = 0; i < 2; i++) {
			mUser = new Contributor();
			mUser.setuserId("id"+i);
			mUser.setRepoName("FEF");
			mUser.setDate(new Date());
			mUser.setRevision(i);
			mUsersList.add(mUser);
		}
		
		return mUsersList;
	}


}
