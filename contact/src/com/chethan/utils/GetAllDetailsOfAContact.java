package com.chethan.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.chethan.contact.MainActivity;
import com.chethan.objects.Person;

class GetAllDetailsOfAContact extends AsyncTask<String, Integer, Person> {

	@Override
	protected Person doInBackground(String... params) {
		MainActivity mainActivity = new MainActivity();
		ContentResolver cr = mainActivity.getContentResolver();

		// using params[0] because i will only pass a single contactid.
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID + " = ?",
				new String[] { params[0] }, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				Person person = new Person();
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

				if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					person.setId(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)));
					person.setName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
					Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
							new String[] { cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)) }, null);
					while (pCur.moveToNext()) {
						person.addPhoneNumber(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
					}
					pCur.close();

					// get email and type

					Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);
					while (emailCur.moveToNext()) {
						// This would allow you get several email addresses
						// if the email addresses were stored in an array
						String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

						System.out.println("Email " + email + " Email Type : " + emailType);
						person.addEmail(emailType, email);
					}
					emailCur.close();

					// Get note.......
					String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
					String[] noteWhereParams = new String[] { id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };
					Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
					if (noteCur.moveToFirst()) {
						String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
						System.out.println("Note " + note);
						person.setTagline(note);
					}
					noteCur.close();

					// Get Postal Address....

					String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
					String[] addrWhereParams = new String[] { id, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
					Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null, null, null, null);
					while (addrCur.moveToNext()) {
						String poBox = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
						String street = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
						String city = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
						String state = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
						String country = addrCur.getString(addrCur
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
						String type = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

						// Do something with these....
						person.setPlace(city);
					}
					addrCur.close();

					// Get Organizations.........

					String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
					String[] orgWhereParams = new String[] { id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE };
					Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI, null, orgWhere, orgWhereParams, null);
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

	@Override
	protected void onPostExecute(Person result) {
		super.onPostExecute(result);

	}

}
