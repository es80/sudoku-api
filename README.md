#### Micronaut AWS API gateway for generating and solving sudokus

A project to try out AWS lambda for an API using Java Micronaut. Based on the example [here](https://github.com/micronaut-projects/micronaut-aws/tree/master/examples/api-gateway-example).

To run locally use [SAM Local](https://github.com/awslabs/aws-sam-local):

```bash
$ sam local start-api --template sam-local.yaml
```

Two `GET` endpoints are provided: `/generate/{boxHeight}/{boxWidth}/{difficulty}` and
`/solve/{boxHeight}/{boxWidth}/{puzzleNumbers}`.

Examples:

```bash
$ curl -s http://127.0.0.1:3000/generate/3/3/tricky
```
returns

```json
{
  "puzzleNums": [0,0,0,0,5,7,...,0],
  "solution": [2,3,8,4,5,7,...,3],
  "numberOfSolutions": "SINGLE_SOLUTION",
  "difficulty": "TRICKY"
}
```

For solving use any of:
```bash
$ curl -s http://127.0.0.1:3000/solve/3/3/0,0,0,0,5,7,  ...
$ curl -s http://127.0.0.1:3000/solve/3/3/000057  ...
$ curl -s http://127.0.0.1:3000/solve/3/3/....57  ...
```

The possible puzzle layouts are box sizes 2x2, 2x3, 2x4, 2x5 and standard 3x3. Difficulties can be `easy`, `medium`, `tricky` or `fiendish` although not all combinations are available.
