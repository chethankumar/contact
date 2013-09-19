package com.chethan.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

import com.chethan.objects.CallHistory;
import com.chethan.objects.Person;
import com.chethan.objects.SimpleContact;

public class ContactService extends Service {

	public final IBinder mBinder = new LocalBinder();
//	public final Messenger mMessenger = new Messenger(new IncomingHandler());

	private static Cursor c;
	private static boolean isDataLoaded = false;
	private static ArrayList<Person> personList = new ArrayList<Person>();
	private static ArrayList<SimpleContact> simpleContactList = new ArrayList<SimpleContact>();
	private static ArrayList<CallHistory> callHistoryList = new ArrayList<CallHistory>();
	
	@Override 
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public class LocalBinder extends Binder {
        public ContactService getService() {
            return ContactService.this;
        }
    }

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Contact service", "Received start id " + startId + ": " + intent);
        Log.d("mylog", "inside onstartcommand. startid : "+startId);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Log.d("mylog", "inside onstartcommand. is data loaded : "+isDataLoaded);
        //readContacts();
        getAllContactsNames();
        populateCallLogData();//populatecalllogdata should be called after allcontact names because of dependency
        return START_STICKY;
    }
	
	public ArrayList<CallHistory> getCallLogData(){
		if(!isDataLoaded)
			populateCallLogData();
		return callHistoryList;
	}
	
	public void populateCallLogData(){
		if(!isDataLoaded){
			Log.d("mylog", "get calllog data isdataloaded is :"+isDataLoaded);
			Uri allCalls = Uri.parse("content://call_log/calls");
			String strOrder = android.provider.CallLog.Calls.DATE + " DESC" + " limit "+99; 
			c = getContentResolver().query(allCalls, null, null, null, strOrder);
			
			int number = c.getColumnIndex(CallLog.Calls.NUMBER);
			int type = c.getColumnIndex(CallLog.Calls.TYPE);
			int date = c.getColumnIndex(CallLog.Calls.DATE);
			if(c.moveToFirst()){
				do {
					CallHistory callHistory = new CallHistory();
					String phNumber = c.getString(number);
					String callType = c.getString(type);
					String callDate = c.getString(date);
					Date callDayTime = new Date(Long.valueOf(callDate));
					int dircode = Integer.parseInt(callType);
					callHistory.setPhoneNumber(phNumber);
					callHistory.setDate(callDayTime);
					callHistory.setType(dircode);
					callHistoryList.add(callHistory);
				} while (c.moveToNext());
			}
			
			//adding contact ids
			
			 for (int i = 0; i < callHistoryList.size(); i++) {
		         Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
		                 Uri.encode(callHistoryList.get(i).getPhoneNumber()));
		         Cursor cFetch = getContentResolver().query(uri,
		                 new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID },
		                 null, null, null);
		         String contactId = "";
		         if (cFetch.moveToFirst()) {
		             cFetch.moveToFirst();
		                 contactId = cFetch.getString(cFetch
		                         .getColumnIndex(PhoneLookup._ID));
		                 callHistoryList.get(i).setId(contactId);
		         }else{
		        	 callHistoryList.get(i).setId(null);
		         }
			 }
			 
			 //adding names and photo
			 for (int i = 0; i < callHistoryList.size(); i++) {
				if (callHistoryList.get(i).getId()!=null) {
					for (int j = 0; j < simpleContactList.size(); j++) {
						if (callHistoryList.get(i).getId().equalsIgnoreCase(simpleContactList.get(j).getId())) {
							callHistoryList.get(i).setName(simpleContactList.get(j).getName());
							callHistoryList.get(i).setPhoto(simpleContactList.get(j).getPhoto());
							break;
						}
					}
				}
			}
		}
		 isDataLoaded=true;
	}

    public ArrayList<String> getContactNameList(){
    	String[] list = {"Susana" 
    	                  ,"Bari"  
    	                  ,"Theresia"  
    	                  ,"Mike"
    	                  ,"Bronwyn"  
    	                  ,"Palmer"  
    	                  ,"Hannah"  
    	                  ,"Kit"  
    	                  ,"Fredericka"  
    	                  ,"Belinda"
    	                  ,"Alishia"  
    	                  ,"Diedra"  
    	                  ,"Rosalia"  
    	                  ,"Vivan"  
    	                  ,"Molly"  
    	                  ,"Alvina"  
    	                  ,"Chas"  
    	                  ,"Marylee"  
    	                  ,"Dolly"  
    	                  ,"Merri"  
    	                  ,"Prudence"  
    	                  ,"Shelli"  
    	                  ,"Jovita"  
    	                  ,"Kathey"  
    	                  ,"Yael"  
    	                  ,"Elouise"  
    	                  ,"Donn"  
    	                  ,"Hilde"  
    	                  ,"Lulu"  
    	                  ,"Justa"  
    	                  ,"Aracely"  
    	                  ,"Ammie"  
    	                  ,"Bert"  
    	                  ,"Janeen"  
    	                  ,"Walton"  
    	                  ,"Mignon"  
    	                  ,"Earline"  
    	                  ,"Silvana"  
    	                  ,"Danae"  
    	                  ,"Alyson"  
    	                  ,"Adelia"  
    	                  ,"Angel"  
    	                  ,"Sommer"  
    	                  ,"Leo"  
    	                  ,"Jaimee"  
    	                  ,"Luci"  
    	                  ,"Kia"  
    	                  ,"Doreen"  
    	                  ,"Clementina"  
    	                  ,"Rebekah"
    	                  ,"Galen"
    	                  ,"Angle"  
    	                  ,"Annie"  
    	                  ,"Stefany"  
    	                  ,"Elvina"  
    	                  ,"Laquanda"  
    	                  ,"Nakia"  
    	                  ,"Ardith"  
    	                  ,"Logan"  
    	                  ,"Tyler"  
    	                  ,"Dorris"  
    	                  ,"Gerri"  
    	                  ,"Paulene"  
    	                  ,"Annalisa"  
    	                  ,"Vesta"  
    	                  ,"Eldora"  
    	                  ,"Beula"  
    	                  ,"Les"  
    	                  ,"Sun"  
    	                  ,"Herman"  
    	                  ,"Hermila"  
    	                  ,"Francisca"  
    	                  ,"Shawnda"  
    	                  ,"Mireille"  
    	                  ,"Catina"  
    	                  ,"Gavin"  
    	                  ,"Yolanda"  
    	                  ,"Carlee"  
    	                  ,"Willian"  
    	                  ,"Lashonda"  
    	                  ,"Calista"  
    	                  ,"Hollis"  
    	                  ,"Setsuko"  
    	                  ,"Markita"  
    	                  ,"Arden"  
    	                  ,"Corrina"  
    	                  ,"Vivien"  
    	                  ,"Edda"  
    	                  ,"Francesco"  
    	                  ,"Alejandra"  
    	                  ,"Gaye"  
    	                  ,"Merrill"  
    	                  ,"Pok"  
    	                  ,"Collene"  
    	                  ,"Lorena"  
    	                  ,"Trena"  
    	                  ,"Larissa"  
    	                  ,"Reatha"  
    	                  ,"Carmina"  
    	                  ,"Thurman"  };
    	
    	//return contactNameList;
    	ArrayList<String> nameList = new ArrayList<String>();
    	for (SimpleContact c : simpleContactList) {
    		if(c.getName()!=null)
    			nameList.add(c.getName());
		}
    	Collections.sort(nameList);
    	return nameList;
    }
    
    public ArrayList<SimpleContact> getSimpleContactsList(){
    	return simpleContactList;
    }
    
    public void getAllContactsNames(){
    	ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
               null, null, null, null);

        if (cur.getCount() > 0) {
           while (cur.moveToNext()) {
        	   SimpleContact contact = new SimpleContact();
        	   String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
               String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    
               if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            	   contact.setName(name);
            	   contact.setId(id);
            	   String image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                   if (image_uri != null) {
                       contact.setPhoto(Uri.parse(image_uri));
                      }
               }
               simpleContactList.add(contact);
           }
        }
        Collections.sort(simpleContactList);
    }
    
    public Person readContact(String contactId){
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
               null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                new String[]{contactId},null);

        if (cur.getCount() > 0) {
           while (cur.moveToNext()) {
        	   Person person = new Person();//check if we can optimize this
               String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
               String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    
               if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                   System.out.println("name : " + name + ", ID : " + id);
                   person.setId(id);
                   person.setName(name);
                   // get the phone <span class="IL_AD" id="IL_AD8">number</span>
                   Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                          ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                          new String[]{id}, null);
                   while (pCur.moveToNext()) {
                         String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                         System.out.println("phone" + phone);
                         person.addPhoneNumber(phone);
                   }
                   pCur.close();


                   // get email and type

                  Cursor emailCur = cr.query(
                           ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                           null,
                           ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                           new String[]{id}, null);
                   while (emailCur.moveToNext()) {
                       // This would allow you get several email addresses
                           // if the email addresses were stored in an array
                       String email = emailCur.getString(
                                     emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                       String emailType = emailCur.getString(
                                     emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                     System.out.println("Email " + email + " Email Type : " + emailType);
                     person.addEmail(emailType, email);
                   }
                   emailCur.close();

                   // Get note.......
                   String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                   String[] noteWhereParams = new String[]{id,
                   ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                           Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                   if (noteCur.moveToFirst()) {
                       String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                     System.out.println("Note " + note);
                     person.setTagline(note);
                   }
                   noteCur.close();

                   //Get Postal Address....

                   String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                   String[] addrWhereParams = new String[]{id,
                       ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                   Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                               null, null, null, null);
                   while(addrCur.moveToNext()) {
                       String poBox = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                       String street = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                       String city = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                       String state = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                       String country = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                       String type = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                       // Do something with these....
                       person.setPlace(city);
                   }
                   addrCur.close();

                   // Get Instant Messenger.........
                   String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                   String[] imWhereParams = new String[]{id,
                       ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                   Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
                           null, imWhere, imWhereParams, null);
                   if (imCur.moveToFirst()) {
                       String imName = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                       String imType;
                       imType = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                   }
                   imCur.close();

                   // Get Organizations.........

                   String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                   String[] orgWhereParams = new String[]{id,
                       ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                   Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                               null, orgWhere, orgWhereParams, null);
                   if (orgCur.moveToFirst()) {
                       String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                       String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                       person.addOrganization(title, orgName);
                   }
                   orgCur.close();
                   
                   String image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                   if (image_uri != null) {
                       person.setPhoto(Uri.parse(image_uri));
                      }
               }
               return person;
           }
      }
		return null;
   }
}
