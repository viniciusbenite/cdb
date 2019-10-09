# Script para adicionar nomes a um SET baseado na letra inicial a partir de um
# ficheiro .txt
# Modo de uso: executar python3 Names.py
# No terminal cat initials4redis.txt | redis-cli --pipe

from string import ascii_lowercase


def initials4redis():
    #ATENCAO ao path do ficheiro!
    path_to_file = '/home/vinicius/Downloads/CDB/Lab_01/female-names.txt'
    redis_file = open('initials4redis.txt', "w")
    with open(path_to_file) as file:
        name = file.readline()
        for name in file:
            for l in ascii_lowercase:
                if name.startswith(l):
                    redis_file.write("SADD {} {}".format(l.upper(), name))
    print("DONE! Results in initials4redis.txt")

initials4redis()