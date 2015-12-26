// Copyright (C) 2015 Tomoaki Takezoe (a.k.a @sumito3478) <sumito3478@gmail.com>
//
// This software is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by the
// Free Software Foundation; either version 3 of the License, or (at your
// option) any later version.
//
// This software is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
// for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this software. If not, see http://www.gnu.org/licenses/.

grammar Jujan;

// Lexical Rules

WS : [ \n\r\t]+ -> skip;

Comment : '//' ~[\r\n\u2028\u2029]* -> channel(HIDDEN);

CommonIdentifier : [_a-zA-Z][_a-zA-Z0-9]*;

RawIdentifier : '\'' ~'\''+ '\'';

Shebang : '#!' ~[\r\n\u2028\u2029]*;

Lang : '#lang ' ~[\r\n\u2028\u2029]*;

Import : '#import ' ~[\r\n\u2028\u2029]*;

RawBinaryOperator : '`' ~'`'+ '`';

SemiColon : ';';

TextLiteral : '"' ~'"'* '"';

fragment DecimalIntegerLiteral : [1-9][0-9]* | '0';

BigIntLiteral : DecimalIntegerLiteral;

Int32Literal : DecimalIntegerLiteral 'i32';

NilLiteral : 'nil';

BooleanLiteral : 'true' | 'false';

// Syntax Rules

literal :
  NilLiteral
  | BooleanLiteral
  | Int32Literal
  | BigIntLiteral
  | TextLiteral
  ;

identifier : CommonIdentifier | RawIdentifier;

qualifiedIdentifier : first=identifier ('::' rest=identifier)*;

recordTypeExpression :
  '{'
  (
    (
      fieldNames=identifier
      ':'
      fieldTypes=typeExpression
      ','
    )*
    lastFieldName=identifier
    ':'
    lastFieldType=typeExpression
  )?
  '}'
  ;

lambdaTypeExpression :
  '('
  (
    (
      parameterNames=identifier
      ':'
      parameterTypes=typeExpression
      ','
    )*
    lastParameterName=identifier
    ':'
    lastParameterType=typeExpression
  )?
  ')'
  '->'
  resultType=typeExpression
  ;

listTypeExpression :
  '['
  (
    (
      parameterTypes=typeExpression
      ','
    )*
    lastParameterType=typeExpression
    lastStar='*'?
  )?
  ']'
  ;

typeExpression :
  lambdaTypeExpression
  | '(' typeExpression ')'
  | listTypeExpression
  | 'typeof' '(' typeofOperand=qualifiedIdentifier ')'
  | left=typeExpression operator='@' right=typeExpression
  | left=typeExpression operator='<:' right=typeExpression
  | left=typeExpression operator='==' righ=typeExpression
  | left=typeExpression operator='&&' right=typeExpression
  | left=typeExpression operator='&' right=typeExpression
  | left=typeExpression operator='|' right=typeExpression
  | recordTypeExpression
  | qualifiedIdentifier
  ;

typeDeclaration :
  'type'
  identifier
  operator=('=' | '@=')
  typeExpression
  ;

lambdaExpressionParameter :
  parameterNames=identifier
  (':' parameterTypeAnnotations=typeExpression)?
  ('=' parameterDefaultValues=expression)?
  ;


lambdaExpression :
  '('
  (
    (lambdaExpressionParameter ',')*
    lambdaExpressionParameter
  )?
  ')'
  ('where' typeConstraint=typeExpression)?
  '->'
  body=expression
  ;

recordExpression :
  '{'
  (
    fieldNames=identifier
    '='
    fieldValues=expression
    ','?
  )*
  '}'
  ;

listExpression :
  '['
  (
    expressions=expression
    ','
  )*
  lastExpression=expression
  ','?
  ']'
  ;


compoundExpression :
  'do'
  '{'
  (
    (
      typeDeclarations=typeDeclaration
      | letDeclarations=letDeclaration
      | declarations=declaration
      | expressions=expression
    )
    ';'
  )*
  lastExpression=expression
  lastSemicolon=';'?
  '}'
  ;

throwExpression :
  'throw' expression;

returnExpression :
  'return' ('to' qualifiedIdentifier)? expression?;

doneExpression :
  'done' expression?;

declaration :
  'generic'?
  'def'
  identifier
  (
    ':'
    typeExpression
  )?
  '='
  expression
  ;

letDeclaration :
  'let'
  identifier
  (
    ':'
    typeExpression
  )?
  '='
  expression
  ;

slotDeclaration :
  'slot'
  identifier
  (
    ':'
    typeExpression
  )?
  '='
  expression
  ;

slotDereferernceExpression :
  '*'
  qualifiedIdentifier
  ;

slotAssignmentExpression :
  slotDereferernceExpression
  '<-'
  expression;

module
  :
  Shebang?
  Lang?
  Import*
  (
    (
      typeDeclarations=typeDeclaration
      | slotDeclarations=slotDeclaration
      | letDeclarations=letDeclaration
      | declarations=declaration
      | expressions=expression
    )
    ';'
  )*
  EOF
  ;

expression
  :
  left=expression binaryOperator='.' right=expression
  | left=expression binaryOperator=('.' | '.?' | '.!') right=expression
  | left=expression binaryOperator='!' right=expression
  | left=expression binaryOperator='@' right=expression
  | lambdaExpression
  | '(' expression ')'
  | literal
  | recordExpression
  | compoundExpression
  | listExpression
  | indexedExpressoin=expression '[' expression ']'
  | appliedFunction=expression
    '('
    (
      (
        parameterNames=identifier
        '='
        parameterValues=expression
        ','
      )*
      (
        (
          lastParameterName=identifier
          '='
        )?
        lastParameterValue=expression
        ','?
      )
    )?
    ')'
  | unaryOperator='++' operand=expression
  | unaryOperator='--' operand=expression
  | unaryOperator='+' operand=expression
  | unaryOperator='-' operand=expression
  | unaryOperator='~' operand=expression
  | unaryOperator='!' operand=expression
  | left=expression binaryOperator=('*' | '/' | '%') right=expression
  | left=expression binaryOperator=('+' | '-' ) right=expression
  | left=expression binaryOperator='++' right=expression
  | left=expression binaryOperator=('<<' | '>>') right=expression
  | left=expression binaryOperator=('<' | '>' | '<=' | '>=') right=expression
  | left=expression binaryOperator=('==' | '!=' ) right=expression
  | left=expression binaryOperator='&' right=expression
  | left=expression binaryOperator='^' right=expression
  | left=expression binaryOperator='|' right=expression
  | left=expression binaryOperator='&&' right=expression
  | left=expression binaryOperator='||' right=expression
  | left=expression binaryOperator=RawBinaryOperator right=expression
  | returnExpression
  | doneExpression
  | slotAssignmentExpression
  | slotDereferernceExpression
  | throwExpression
  | qualifiedIdentifier
  ;
