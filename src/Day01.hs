import Data.Char (isDigit)
import Data.List (find, tails)
import Data.Maybe (fromJust, mapMaybe)

getFirstDigit1 :: String -> Char
getFirstDigit1 s = fromJust $ find isDigit s

getNumber1 :: String -> Int
getNumber1 s = read $ getFirstDigit1 s : [getFirstDigit1 (reverse s)]

tryReadDigit :: String -> Maybe Int
tryReadDigit (c:s) | isDigit c = Just $ read [c]
tryReadDigit ('o':'n':'e':s) = Just 1
tryReadDigit ('t':'w':'o':s) = Just 2
tryReadDigit ('t':'h':'r':'e':'e':s) = Just 3
tryReadDigit ('f':'o':'u':'r':s) = Just 4
tryReadDigit ('f':'i':'v':'e':s) = Just 5
tryReadDigit ('s':'i':'x':s) = Just 6
tryReadDigit ('s':'e':'v':'e':'n':s) = Just 7
tryReadDigit ('e':'i':'g':'h':'t':s) = Just 8
tryReadDigit ('n':'i':'n':'e':s) = Just 9
tryReadDigit _ = Nothing

getNumber :: String -> Int
getNumber s =
  let digits = mapMaybe tryReadDigit $ tails s
  in head digits * 10 + last digits

main1 :: IO ()
main1 = do
  input <- getContents
  let allLines = lines input
      numbers = map getNumber1 allLines
  print $ sum numbers

main :: IO ()
main = do
  input <- getContents
  let allLines = lines input
      numbers = map getNumber allLines
  print $ sum numbers
