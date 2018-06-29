package com.example.android.invapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** Database helper that will provide us access to the database */
    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new BookDbHelper(this);
    }

/**
 * API Contract for the Pets app.
 */
public final class BookContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {}

    /**
     * Inner class that defines constant values for the book database table.
     * Each entry in the table represents a single book.
     */
    public final class BookEntry implements BaseColumns {

        /** Name of database table for books */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the book.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME ="productName";

        /**
         * Price of the book.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Book quantity.
         *
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * Supplier name.
         *
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME ="supplierName";

        /**
         * Supplier phone number.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_PHONE ="supplierPhone";
    }

}
    /**
     * Database helper for Pets app. Manages database creation and version management.
     */
    public class BookDbHelper extends SQLiteOpenHelper {

        public final String LOG_TAG = BookDbHelper.class.getSimpleName();

        /** Name of the database file */
        private static final String DATABASE_NAME = "products.db";

        /**
         * Database version. If you change the database schema, you must increment the database version.
         */
        private static final int DATABASE_VERSION = 1;

        /**
         * Constructs a new instance of {@link BookDbHelper}.
         *
         * @param context of the app
         */
        public BookDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * This is called when the database is created for the first time.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create a String that contains the SQL statement to create the books table
            String SQL_CREATE_BOOKS_TABLE =  "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("
                    + BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BookContract.BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                    + BookContract.BookEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                    + BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                    + BookContract.BookEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                    + BookContract.BookEntry.COLUMN_SUPPLIER_PHONE + " TEXT);";

            // Execute the SQL statement
            db.execSQL(SQL_CREATE_BOOKS_TABLE);
            Log.e(LOG_TAG, "tables crées" + BookContract.BookEntry.COLUMN_PRODUCT_NAME);
        }
        

        /**
         * This is called when the database needs to be upgraded.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // The database is still at version 1, so there's nothing to do be done here.
        }
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertBook() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Mon livre's book attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_PRODUCT_NAME, "Mon livre");
        values.put(BookContract.BookEntry.COLUMN_PRODUCT_PRICE, "10");
        values.put(BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY,"1");
        values.put(BookContract.BookEntry.COLUMN_SUPPLIER_NAME, "Mr X");
        values.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE, "00 56 00 56");

        // Insert a new row for Mon livre test in the database, returning the ID of that new row.
        // The first argument for db.insert() is the books table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Mon livre.
        long newRowId = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);

        Log.e(LOG_TAG, "dummy values entered");
    }

    @Override
    protected void onStart() {
        super.onStart();
        insertBook();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to query information about the state of
     * the books database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_PRODUCT_NAME,
                BookContract.BookEntry.COLUMN_PRODUCT_PRICE,
                BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY,
                BookContract.BookEntry.COLUMN_SUPPLIER_NAME,
                BookContract.BookEntry.COLUMN_SUPPLIER_PHONE,
        };

        // Perform a query on the books table
        Cursor cursor = db.query(
                BookContract.BookEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_view_book);

        try {
            displayView.setText("The books table contains " + cursor.getCount() + " books.\n\n");
            displayView.append(BookContract.BookEntry._ID + " - " +
                    BookContract.BookEntry.COLUMN_PRODUCT_NAME + " - " +
                    BookContract.BookEntry.COLUMN_PRODUCT_PRICE + " - " +
                    BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    BookContract.BookEntry.COLUMN_SUPPLIER_NAME + " - " +
                    BookContract.BookEntry.COLUMN_SUPPLIER_PHONE + " - ");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
            int productNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentProductName = cursor.getString(productNameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);
            // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentProductName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
}
