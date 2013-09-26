This is a version of the alarm example from the VDM++ book, John 
Fitzgerald, Peter Gorm Larsen, Paul Mukherjee, Nico Plat and Marcel 
Verhoef, Validated Designs for Object-oriented Systems, Springer, 
New York. 2005, ISBN 1-85233-881-4. This version of the example has 
been used for proof purposes and thus the operations in the normal 
VDM++ of this example are made as functions just like in the VDM-SL
version of the alarm example. The example is inspired by a subcomponent 
of a large alarm system developed by IFAD A/S. It is modelling the 
management of alarms for an industrial plant. The purpose of the 
model is to clarify the rules governing the duty roster and calling 
out of experts to deal with alarms. 

#******************************************************
#  AUTOMATED TEST SETTINGS
#------------------------------------------------------
#AUTHOR= John Fitzgerald and Peter Gorm Larsen
#LANGUAGE_VERSION=classic
#INV_CHECKS=true
#POST_CHECKS=true
#PRE_CHECKS=true
#DYNAMIC_TYPE_CHECKS=true
#SUPPRESS_WARNINGS=false
#ENTRY_POINT=new alarm().RunTest()
#EXPECTED_RESULT=NO_ERROR_TYPE_CHECK
#******************************************************