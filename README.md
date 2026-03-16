# Stock Predictor (Java + Maven)

一个标准的 Maven Java 工程，用于：

1. 拉取股票历史日线（K 线）数据（示例：`300779`）。
2. 基于技术指标（MA、RSI、量能）预测下一交易日涨跌方向。

> 该项目用于学习和工程示例，不构成投资建议。

## 技术栈

- Java 11
- Maven 3.8+
- Jackson (`jackson-databind`) 解析 JSON
- JUnit 5 单元测试

## 工程结构与代码职责

```text
src
├── main
│   └── java/com/example/stockpredictor
│       ├── app
│       │   └── Application.java
│       ├── client
│       │   └── EastMoneyClient.java
│       ├── model
│       │   ├── KLine.java
│       │   └── PredictionResult.java
│       └── service
│           ├── IndicatorCalculator.java
│           └── SimpleTrendPredictor.java
└── test
    └── java/com/example/stockpredictor/service
        └── SimpleTrendPredictorTest.java
```

- `Application`：程序入口，解析参数、组装调用链、输出预测结果。
- `EastMoneyClient`：请求东方财富 K 线接口，并解析成 `KLine` 列表。
- `KLine`：单日行情数据模型（开收高低量、日期）。
- `PredictionResult`：预测输出模型（MA、RSI、上涨概率、方向判断）。
- `IndicatorCalculator`：技术指标计算（MA、成交量均值、RSI）。
- `SimpleTrendPredictor`：核心策略，基于指标进行打分并映射为上涨概率。
- `SimpleTrendPredictorTest`：验证预测概率区间正确性（0~1）。

## 预测流程说明

1. 获取历史日线数据（默认前复权日 K）。
2. 计算关键指标：`MA5`、`MA10`、`MA20`、`RSI14`、近 5 日量能变化。
3. 使用简单打分逻辑：
   - 趋势关系（价格与均线、均线多空排列）
   - RSI 超买超卖
   - 当日动量（涨跌幅）
   - 量价配合
4. 通过 Sigmoid 函数将分数映射成上涨概率。
5. 规则：`upProbability >= 0.5` 视为“次日上涨”，否则“次日下跌”。

## 环境要求

- JDK 11+
- Maven 3.8+

## 编译与测试

```bash
mvn clean test
```

## 运行

默认预测股票 `300779`：

```bash
mvn exec:java
```

指定参数运行：

```bash
mvn exec:java -Dexec.args="300779 20220101 20991231"
```

参数说明：

1. `stockCode`：股票代码（如 `300779`）
2. `beginDate`：开始日期，格式 `yyyyMMdd`
3. `endDate`：结束日期，格式 `yyyyMMdd`

## 输出示例

程序输出包括：

- 最新交易日与收盘价
- 指标值：`MA5` / `MA10` / `MA20` / `RSI14`
- 次日上涨概率（百分比）
- 次日预测方向：`UP` 或 `DOWN`

## 可扩展方向

- 增加回测模块（统计历史预测命中率）
- 引入更多特征（波动率、换手率、行业因子等）
- 改造为 REST API（Spring Boot）
- 使用机器学习模型替代规则打分（如逻辑回归、树模型）

## 风险提示

- 当前策略是简化版演示，未包含完整交易成本、滑点和风险控制。
- 实盘前请先做充分回测和参数验证。
