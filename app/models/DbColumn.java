package models;

/**
 *
 *<P>Each column description has the following columns:
  <OL>
        <LI><B>COLUMN_NAME</B> String => column name
        <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
        <LI><B>TYPE_NAME</B> String => Data source dependent type name,
  for a UDT the type name is fully qualified
        <LI><B>COLUMN_SIZE</B> int => column size.
        <LI><B>BUFFER_LENGTH</B> is not used.
        <LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits. Null is returned for data types where
 DECIMAL_DIGITS is not applicable.
        <LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
        <LI><B>NULLABLE</B> int => is NULL allowed.
      <UL>
      <LI> columnNoNulls - might not allow <code>NULL</code> values
      <LI> columnNullable - definitely allows <code>NULL</code> values
      <LI> columnNullableUnknown - nullability unknown
      </UL>
        <LI><B>REMARKS</B> String => comment describing column (may be <code>null</code>)
        <LI><B>COLUMN_DEF</B> String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be <code>null</code>)
        <LI><B>CHAR_OCTET_LENGTH</B> int => for char types the
       maximum number of bytes in the column
        <LI><B>ORDINAL_POSITION</B> int => index of column in table
      (starting at 1)
        <LI><B>IS_NULLABLE</B> String  => ISO rules are used to determine the nullability for a column.
       <UL>
       <LI> YES           --- if the parameter can include NULLs
       <LI> NO            --- if the parameter cannot include NULLs
       <LI> empty string  --- if the nullability for the
 parameter is unknown
       </UL>
  </OL>
 *
 */
public class DbColumn {
	public String column_name;
	public int data_type;
	public String type_name;
	public int column_size;
	public int decimal_digits;
	public int num_prec_radix;
	public int nullable;
	public String remarks;
	public int char_octet_length ;
    public String is_nullable ;



}