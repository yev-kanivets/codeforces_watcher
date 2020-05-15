//
//  ChartMarker.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import Charts
import common

class ChartMarker: MarkerView {

    var text = ""

    override func refreshContent(entry: ChartDataEntry, highlight: Highlight) {
        super.refreshContent(entry: entry, highlight: highlight)
        guard let chartItem = entry.data as? ChartItem else { fatalError() }

        var ratingChange = chartItem.ratingChange
        ratingChange = ratingChange.starts(with: "-") ? ratingChange : "+" + ratingChange

        let splittedPair = StringExtensionsKt.splitStringInHalf(chartItem.contest)
        let (contestFirstHalf, contestSecondHalf) = (String(splittedPair.first ?? ""), String(splittedPair.second ?? ""))

        text = "chart_info".localizedFormat(args: chartItem.rating, ratingChange, chartItem.rank, contestFirstHalf, contestSecondHalf)
    }

    override func draw(context: CGContext, point: CGPoint) {
        super.draw(context: context, point: point)

        var drawAttributes = [NSAttributedString.Key: Any]()
        drawAttributes[.font] = Font.textBody
        drawAttributes[.foregroundColor] = Palette.white
        drawAttributes[.backgroundColor] = Palette.darkGrey

        self.bounds.size = (text as NSString).size(withAttributes: drawAttributes)
        self.offset = CGPoint(x: 0, y: -self.bounds.size.height - 2)

        let offset = self.offsetForDrawing(atPoint: point)

        drawText(text: " \(text) " as NSString, rect: CGRect(origin: CGPoint(x: point.x + offset.x, y: point.y + offset.y), size: self.bounds.size), withAttributes: drawAttributes)
    }

    private func drawText(text: NSString, rect: CGRect, withAttributes attributes: [NSAttributedString.Key: Any]? = nil) {
        let size = text.size(withAttributes: attributes)
        let centeredRect = CGRect(
            x: rect.origin.x + (rect.size.width - size.width) / 2.0,
            y: rect.origin.y + (rect.size.height - size.height) / 2.0,
            width: size.width,
            height: size.height
        )
        text.draw(in: centeredRect, withAttributes: attributes)
    }
}

class xAxisFormatter: IAxisValueFormatter {
    
    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        let date = Date(timeIntervalSince1970: value)
        let dayTimePeriodFormatter = DateFormatter().apply {
            $0.dateFormat = "MMM, yyyy"
        }
        return dayTimePeriodFormatter.string(from: date)
    }
}
