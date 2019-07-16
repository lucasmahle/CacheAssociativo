

# Memória Cache Associativa por Conjunto

**Aluno: Lucas Sérgio Mahle**
**Matéria: Organização de computadores**
**Professor: Luciano Lores Caimi**
**Data: Julho/2019**

## Descrição	
Esse é um projeto acadêmico com objetivo de simular uma memória cache associativo, o código fonte esta focado em enfatizar a funcionalidade sem preocupação no sentido de qualidade do código ou boas práticas.

## Requisitos
Número de células na MP: 128
Tamanho do bloco: 4 células
Número de linhas na cache: 8
Tamanho da célula: 8 bits
Tamanho do conjunto: 2 linhas

Cada aluno deve implementar a política de mapeamento, substituição e escrita conforme:

Na tela do programa deve ser apresentado todo o conteúdo da memória principal, da memória cache, da próxima localização que será substituída (de acordo com a política definida), além de um menu que de acesso às seguintes operações:
- Ler o conteúdo de um endereço da memória
- Escrever em um determinado endereço da memória
- Apresentar as estatísticas de acertos e faltas (absolutos e percentuais) para as três situações: leitura, escrita e geral
- Encerrar o programa

Os valores e endereços devem ser apresentados em hexadecimal ou binário.
Ao ler um endereço deve informar se encontrou na cache ou não.